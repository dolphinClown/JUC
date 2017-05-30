package com.clay.impl;

import org.junit.Test;

/**
 * Created by CLAY on 2017/5/30.
 */
public class TestThreadPool {
    @Test
    public void testThreadPool() {
        MyThreadPool<Job> pool = new MyThreadPool<>();
        for (int i = 0; i < 10; i++) {
            //添加 10 个任务
            pool.execute(new Job());
        }
        System.out.println(pool.getJobSize());
    }

    class Job implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("执行" + i);
            }
        }
    }

}
