package com.clay.juc02;

/**
 * 等待唤醒机制
 * 虚假唤醒；API中 查看 wait（）
 */
public class TestProductAndConsumer {
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Productor productor = new Productor(clerk);
        Consumer consumer = new Consumer(clerk);

        new Thread(productor, "生产者1：").start();
        new Thread(productor, "生产者2：").start();
        new Thread(consumer, "消费者1：").start();
        new Thread(consumer, "消费者2：").start();
    }
}

//店员
class Clerk {

    //共享资源
    private int product = 0;

    //进货
    public synchronized void get() {
        //每次生产一件，可以修改数量进行调整
        while (product >= 1) { //为了避免虚假唤醒问题，wait()应该总是用在循环中
//            System.out.println("产品充足！");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " 剩余: " + ++product);
        this.notifyAll();
    }

    //卖货
    public synchronized void sale() {
        while (product <= 0) {
//            System.out.println("产品不足！");
            try {
                //使用if做判断，出现问题：
                //当多个线程发现product<=0时，同时wait(),挂起，此时，当生产者线程 notifyAll()，
                //多个等待的线程被唤醒，将往下执行，进行了多次 --product, 会出现线程安全问题，这就是虚假唤醒
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " 剩余: " + --product);
        this.notifyAll();
    }
}

//生产者
class Productor implements Runnable {

    private Clerk clerk;

    public Productor(Clerk clerk) {
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
class Consumer implements Runnable {

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}
