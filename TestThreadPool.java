package com.clay.juc02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 第四种创建线程的方法：线程池（Executor）
 * 提供了一个线程队列，队列中保存着所有等待状态的线程。避免了创建与销毁的额外开销,并提高响应速度
 * 1.线程池的体系结构：
 * java.util.concurrent.Executor:负责线程的使用和调度的根接口
 *      \--【ExecutorService】 子接口：线程池的主要接口
 *          \--ThreadPoolExecutor 实现类
 *          \--【ScheduledExecutorservice】 子接口：负责线程的调度
 *              \--ScheduledThreadPoolExecutor: extends ThreadPoolExecutor implements ScheduledExecutorservice
 * 2.工具类：Executors
 * ExecutorService newFixedThreadPool(): 创建固定大小的线程池
 * ExecutorService newCachedThreadPool(): 缓存线程池，线程池数量不固定，可以根据需求自动更新数量
 * ExecutorService newSingleThreadExecutor(): 创建单个线程池。线程池中仅有一个线程
 *
 * ScheduleExecutorService newScheduleThreadPool(): 创建固定大小的线程池，可以延迟或定时的执行任务，进行线程调度。
 */
public class TestThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);

        List<Future<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int sum = 0;

                    for (int i = 0; i < 100; i++) {
                        sum += i;
                    }
                    return sum;
                }
            });
            list.add(future);
        }
        pool.shutdown();
//        System.out.println(list);
        for (Future<Integer> future: list) {
            System.out.println(future.get());
        }

//        //1.创建线程池
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        ThreadPoolDemo tpd = new ThreadPoolDemo();
//        //2.为线程池中的线程分配任务
//        for (int i = 0; i < 10; i++) {
//            pool.submit(tpd);
//        }
//
//        //3.关闭线程池
//        pool.shutdown();
    }
}

class ThreadPoolDemo implements Runnable{

    private int i = 0;

    @Override
    public void run() {
        while (i <= 100) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}