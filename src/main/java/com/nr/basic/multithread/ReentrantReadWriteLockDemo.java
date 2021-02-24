package com.nr.basic.multithread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

    static class ReadThread extends Thread{

        private ReentrantReadWriteLock rrwLock;

        public ReadThread(String name,ReentrantReadWriteLock rrwLock) {
            super(name);
            this.rrwLock = rrwLock;
        }

        public void run(){
            System.out.println(Thread.currentThread().getName() + " trying to lock");
            try {
                rrwLock.readLock().lock();
                System.out.println(Thread.currentThread().getName() + " lock successfully");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rrwLock.readLock().unlock();
                System.out.println(Thread.currentThread().getName() + " unlock successfully");
            }
        }
    }

    static class WriteThread extends Thread{

        private ReentrantReadWriteLock rrwLock;

        public WriteThread(String name,ReentrantReadWriteLock rrwLock) {
            super(name);
            this.rrwLock = rrwLock;
        }

        public void run(){
            System.out.println(Thread.currentThread().getName() + " trying to lock");
            try {
                rrwLock.writeLock().lock();
                System.out.println(Thread.currentThread().getName() + " lock successfully");
            } finally {
                rrwLock.writeLock().unlock();
                System.out.println(Thread.currentThread().getName() + " unlock successfully");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        new ReadThread("r1",lock).start();
        new ReadThread("r2",lock).start();
        Thread.sleep(30000000L);
        new WriteThread("w1",lock).start();
    }
}
