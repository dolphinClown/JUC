package com.clay.juc02;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 一、创建执行线程的方式三： 实现Callable;
 * 相较于实现 Runnable，多了返回值和泛型
 * 二、执行 Calllable 方式，需要FutureTask（Future接口的实现类）实现类的支持，用于接受运算结果
 */
public class TestCallable {
    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();

        //1.执行Calllable方式，需要FutureTask（Future接口的实现类）实现类的支持，用于接受运算结果
        FutureTask<Integer> result = new FutureTask<Integer>(td);

        new Thread(result).start();

        //2.接受线程运算结果,仅当线程执行完成才能获取到，所以FutureTask也可以用于闭锁操作
        try {
            Integer sum = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class ThreadDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            sum += 1;
        }
        return sum;
    }
}