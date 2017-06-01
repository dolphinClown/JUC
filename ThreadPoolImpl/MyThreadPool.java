package com.clay.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by CLAY on 2017/5/30.
 */
public class MyThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    //默认工作线程数量
    private static final int DEFAULT_WORKERS = 5;

    //最小工作线程数量限制
    private static final int MIN_WORKERS = 1;

    //最大工作线程数量限制
    private static final int MAX_WORKERS = 10;

    //任务列表，用于存放的工作任务
    private final LinkedList<Job> jobs = new LinkedList<Job>();

    //工作者列表
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());

    //工作线程数量
    private int workerNum;

    //生成线程编号
    private AtomicLong threadNum = new AtomicLong();

    public MyThreadPool() {
        initializeWorkers(DEFAULT_WORKERS);
    }

    public MyThreadPool(int num) {
        this.workerNum = num > MAX_WORKERS ? MAX_WORKERS : num < MIN_WORKERS ? MIN_WORKERS : num;
        initializeWorkers(num);
    }

    //初始化，启动相应数量的工作线程
    private void initializeWorkers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    @Override
    public void execute(Job job) {
        if (job != null) {
            //添加一个任务，并通知工作者线程执行
            synchronized (jobs) {
                jobs.addLast(job);
                //每次唤醒一个，防止等待队列中的线程全部移动到阻塞队列，减少开销
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    //在不超过最大限制线程数量的条件下，添加工作线程
    @Override
    public void addWorkers(int num) {
        synchronized (jobs) {
            if (num + this.workerNum > MAX_WORKERS) {
                num = MAX_WORKERS - this.workerNum;
            }
            initializeWorkers(num);
            this.workerNum += num;
        }
    }

    //减少工作线程的数量，不少于最少线程数量
    @Override
    public void removeWorker(int num) {
        synchronized (jobs) {
            if (num > workerNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            //按照给定数量停止工作线程
            int count = 0;
            while (count < num) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    class Worker implements Runnable {
        //标记，用于终止线程
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            //感知外部的中端操作，返回
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    //取出一个任务
                    job = jobs.removeFirst();
                }
                if (job != null)
                    try {
                        job.run();
                    } catch (Exception e) {
                        //忽略 Job 执行中的异常
                    }
            }
        }

        public void shutdown() {
            this.running = false;
        }
    }

}
