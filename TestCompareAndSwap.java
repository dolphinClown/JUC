package com.clay.juc01;

/**
 * Created by CLAY on 2017/3/21.
 * CAS（Compare-And-Swap）算法保证数据原子性
 * CAS算法是【硬件】对于并发操作共享数据的支持
 *        CAS包含了三个操作数：
 *            内存位置 V（在Java中可以简单理解为变量的内存地址）、
 *            旧的预期值 A（要进行运算时再次从内存中读取的值）、拟写入的值 B（运算得到的值）
 *            当且仅当 V==A 时， V = B （将B赋给V）,否则将不做任何操作
 */
public class TestCompareAndSwap {
    public static void main(String[] args) {
        final  CompareAndSwap cas = new CompareAndSwap();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //运算前从内存中取到的值 作为预期值
                    int expectedValue = cas.get();
                    boolean b = cas.compareAndSet(expectedValue, (int) Math.random() * 11);
                    System.out.println(b);
                }
            }).start();
        }
    }
}

class CompareAndSwap{

    private int value;

    //获取内存值
    public synchronized int get() {
        return value;
    }

    /**
     * 比较当前内存值和旧的预期值，只有两个值相等的情况，进行更新
     * @param expectedValue 旧的预期值 - 在进行运算前从内存中读取的值
     * @param newValue      拟写入的新值 - 运算得到的值，即拟写入内存的值
     * @return
     */
    public synchronized int compareAndSwap(int expectedValue, int newValue){
        int oldValue = value;

        //比较当前内存值和旧的预期值 如果相等，将更新值赋给内存值
        if (oldValue == expectedValue) {
            this.value = newValue;
        }

        return oldValue;
    }

    //设置
    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
