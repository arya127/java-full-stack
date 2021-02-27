package com.dev.fullstack.multithread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JucTools {

    private  CountDownLatch countDownLatch = new CountDownLatch(2);
    private  ExecutorService executorService = Executors.newFixedThreadPool(8);

    public void countDownLatch(){
        executorService.execute(() -> {
            try {
                Thread.sleep(50);
                System.out.println("t1的name为"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                Thread.sleep(50);
                System.out.println("t2的name为"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        JucTools tools = new JucTools();
        tools.countDownLatch();
        tools.countDownLatch.await();
        System.out.println("main的name为"+Thread.currentThread().getName());
        tools.executorService.shutdown();
    }
}
