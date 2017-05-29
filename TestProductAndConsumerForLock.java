package com.clay.juc02;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 对象中，与wait() notify() notifyAll()方法相对应的
 *                   有await() signal() signalAll()
 * Condition实例实质上被绑定到一个锁上。要为特定lock实例获取Condition
 * 实例，使用newCondition()方法
 */
public class TestProductAndConsumerForLock {
    public static void main(String[] args) {
        Clerk1 clerk = new Clerk1();
        Productor1 productor = new Productor1(clerk);
        Consumer1 consumer = new Consumer1(clerk);

        new Thread(productor, "生产者1：").start();
        new Thread(productor, "生产者2：").start();
        new Thread(consumer, "消费者1：").start();
        new Thread(consumer, "消费者2：").start();
    }
}

//店员
class Clerk1 {

    private int product = 0;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    //进货
    public void get() {
        lock.lock();
        try {
            while (product >= 3) { //为了避免虚假唤醒问题，wait()应该总是用在循环中
    //            System.out.println("产品充足！");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " 剩余: " + ++product);
            condition.signalAll();
        } finally {
                lock.unlock();
        }
    }

    //卖货
    public  void sale() {
        lock.lock();
        try {
            while (product <= 0) {
    //            System.out.println("产品不足！");
                try {
                    //使用if做判断：
                    //当多个线程发现product<=0时，同时wait(),此时，当生产者线程notifyAll()，
                    //多个等待的线程被唤醒，将往下执行，进行了多次--product,会出现问题，这就是虚假唤醒
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " 剩余: " + --product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

//生产者
class Productor1 implements Runnable {

    private Clerk1 clerk;

    public Productor1(Clerk1 clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.get();
        }
    }
}

//消费者
class Consumer1 implements Runnable {

    private Clerk1 clerk;

    public Consumer1(Clerk1 clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}
