package com.nr.basic.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo {

    private volatile WaitNode waiters = new WaitNode(2);

    public void f(){
        FutureTask<Integer> futureTask = new FutureTask<>(new Task());
        Thread thread1 = new Thread(futureTask,"t1");
        //Thread thread2 = new Thread(futureTask,"t2");
        thread1.start();
        //thread2.start();
        new Thread(() -> {
            try {
                futureTask.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"q1").start();
        new Thread(() -> {
            try {
                futureTask.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"q2").start();
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

    public static void main(String[] args) {
        FutureTaskDemo demo = new FutureTaskDemo();
        demo.f2();
    }

    static class Task  implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("Thread [" + Thread.currentThread().getName() + "] is running");
            int result = 0;
            for(int i = 0; i < 100;++i) {
                result += i;
            }

            Thread.sleep(3000000000L);
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
