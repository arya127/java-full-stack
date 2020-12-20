package org.example;

import org.junit.Test;
import java.util.concurrent.*;

public class ThreadTest {

    public static Future<Integer> calculate(Integer input,ExecutorService executor) {
        return executor.submit(() -> {
            Thread.sleep(3000);
            return input * input;
        });
    }

    @Test
    public void test1() {
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
}
