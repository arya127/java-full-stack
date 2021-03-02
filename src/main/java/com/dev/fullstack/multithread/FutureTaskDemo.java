package com.dev.fullstack.multithread;

import java.util.concurrent.*;

public class FutureTaskDemo {

    private volatile WaitNode waiters = new WaitNode(2);

    public void f() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Task());
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(futureTask);
//        Thread thread1 = new Thread(futureTask,"t1");
//        thread1.start();
        System.out.println(futureTask.get());
        executor.shutdown();
        //测试FutureTask的get方法阻塞性
//        new Thread(() -> {
//            try {
//                futureTask.get();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        },"q1").start();
//        new Thread(() -> {
//            try {
//                futureTask.get();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        },"q2").start();
    }

    public void f2(){
        WaitNode q = new WaitNode(1);
        WaitNode p = waiters;
        p.setValue(3);
        waiters = new WaitNode(5);
        q.next = waiters;
        if(waiters == waiters){
            waiters = q;
        }
    }

    public static void main(String[] args) throws Exception {
        FutureTaskDemo demo = new FutureTaskDemo();
        demo.f();
    }

    static class Task  implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("Thread [" + Thread.currentThread().getName() + "] is running");
            int result = 0;
            for(int i = 0; i < 100;++i) {
                result += i;
            }
            Thread.sleep(3000L);
            return result;
        }
    }

    static final class WaitNode {
        volatile Integer value;
        volatile WaitNode next;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public WaitNode getNext() {
            return next;
        }

        public void setNext(WaitNode next) {
            this.next = next;
        }

        WaitNode(Integer value) { this.value = value; }
    }
}
