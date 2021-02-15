package org.example;

import org.junit.Test;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {

    public static Future<Integer> calculate(Integer input,ExecutorService executor) {
        return executor.submit(() -> {
            Thread.sleep(3000);
            return input * input;
        });
    }

    @Test
    public void test1() {
        Thread thread = new Thread();
        thread.interrupt();
        thread.isInterrupted();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Long time = System.currentTimeMillis();
        try {
            calculate(100,executor).get(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("用时"+(System.currentTimeMillis()-time));
    }

    @Test
    public void test2(){
        final byte b1=1;
        final byte b2=3;
        byte b3= b1+b2;
    }

    @Test
    public void test3() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (;;){
                //System.out.println("hello");
            }
        });
        thread.start();
        thread.interrupt();
        System.out.println("is"+thread.isInterrupted());
        System.out.println("is"+thread.interrupted());
        System.out.println("is"+Thread.interrupted());
        System.out.println("is"+thread.isInterrupted());
        thread.join();
        System.out.println("over");
    }

    @Test
    public void aqsTest(){
        AbstractQueuedSynchronizer aqs = new AbstractQueuedSynchronizer() {
        };
    }
}
