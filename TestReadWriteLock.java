package com.clay.juc02;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：
 * 写写/读写，需要互斥
 * 读读 不需要互斥
 * 允许多个线程读，同时最多一个线程去写
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        ReadWriterLockDemo readWriteLockDemo = new ReadWriterLockDemo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readWriteLockDemo.set((int) Math.random() + 101);
            }
        }).start();

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readWriteLockDemo.get();
                }
            }).start();
        }


    }
}

class ReadWriterLockDemo {

    private int number = 0;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

     //读
    public void get() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + ":" + number);
        } finally {
            lock.readLock().unlock();
        }
    }

    //写
    public void set(int number) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName());
            this.number = number;
        } finally {
            lock.writeLock().unlock();
        }
    }


}