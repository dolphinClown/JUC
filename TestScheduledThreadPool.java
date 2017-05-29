package com.clay.juc02;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by CLAY on 2017/3/22.
 */
public class TestScheduledThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        for (int i = 0; i < 5; i++) {
            Future<Integer> result =  pool.schedule(new Callable<Integer>() {
                 @Override
                 public Integer call() throws Exception {
                     int num = new Random().nextInt(100);
                     System.out.println(Thread.currentThread().getName() + ":" + num);

                     return num;
                 }
             },3, TimeUnit.SECONDS);

            System.out.println(result.get());
        }


        pool.shutdown();
    }
}
