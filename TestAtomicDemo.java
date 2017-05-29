package com.clay.juc01;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一、i++的原子性问题(读-改-写)：
 *     int i = 10;
 *     i = i++; //10
 *     底层实现为：
 *     int temp(i++) = i;
 *     i = i + 1;
 *     i = temp;
 *
 * 二、原子变量：jdk1.5后 提供了大量原子变量
 *      1.volatile 保证内存可见性
 *      2.CAS（Compare-And-Swap）算法保证数据原子性
 *        CAS算法是【硬件】对于并发操作共享数据的支持
 *        CAS包含了三个操作数：
 *            内存值 V（内存中变量的位置）、预估值 A（要进行运算时再次从内存中读取的值）、更新值 B（运算得到的值）
 *            当且仅当 V==A 时， V = B （将B赋给V）,否则将不做任何操作
 */
public class TestAtomicDemo {
    public static void main(String[] args) {
        AtomicDemo atomicDemo = new AtomicDemo();

        for (int i = 0; i < 10; i++) {
            new Thread(atomicDemo).start();
        }

    }
}

class AtomicDemo implements Runnable {
//        private volatile int serialNumber = 0;
    private AtomicInteger serialNumber = new AtomicInteger();

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+ "--" +getSerialNumber());
    }

    public int getSerialNumber() {
//        return serialNumber++;
        return serialNumber.getAndIncrement(); //后++
    }

}
