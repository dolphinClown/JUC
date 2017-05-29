package com.clay.juc01;

/**
 * 轻量级同步机制
 * volatile ：当多个线程进行操作共享数据时，共享数据内存可见。并且禁止指令重排序
 * 1.不具备“互斥性”
 * 2.不能保证“原子性”
 *
 * 使用volatile应该具备以下两个条件：
 * 1、对变量的操作不依赖当前值（例如：自增自减等不具备原子性的操作）
 * 2、对变量的操作中不包含其他变量的不变式
 */
public class TestVolatile {
    public static void main(String[] args) {

        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();

        //执行效率很高，以至于 mian 线程 没有机会再次从主存中读取更新状态。
        while (true) {
            //读
            if (td.ifFlag()) {
                System.out.println("------------------");
                break;
            }
        }
    }

}

class ThreadDemo implements Runnable {

//    private boolean flag = false;
    private volatile boolean flag = false;

    @Override
    public void run() {
        //写
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = true;
        System.out.println("flag:" + flag);
    }

    public boolean ifFlag(){
        return flag;
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }
}
