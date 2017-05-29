package com.clay.juc02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一、用与解决多线程安全问题的方式：
 * synchronized（隐式锁）： 1. 同步代码块   2. 同步方法
 *
 *             jdk 1.5 后：
 *                         3.同步锁 Lock
 *                         注意：是一个显示锁，需要通过 lock() 方法上锁，unlock() 释放
 *                         unlock 必须放的 finaly 中执行，防止出现线程安全问题
 */
public class TestLock {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(ticket, "1号窗口").start();
        new Thread(ticket, "2号窗口").start();
        new Thread(ticket, "3号窗口").start();
    }
}

class Ticket implements Runnable {

    private int ticket = 100;

    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock(); //上锁

            try {
                if (ticket > 0) {
                    try {
                        //放大线程执行
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "售出一张票，还剩：" + ticket--);
                }else{
                    break;
                }

            } finally {
                lock.unlock(); //释放锁
            }
        }
    }
}