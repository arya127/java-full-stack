package com.dev.fullstack.multithread;

public class ReentrantLockDemo {

    static class Product {
        private Depot depot;

        public Product(Depot depot) {
            this.depot = depot;
        }

        public void product(int no) {
            new Thread(() -> depot.produce(no),no+"produce thread").start();
        }
    }

    static class Consumer {
        private Depot depot;

        public Consumer(Depot depot) {
            this.depot = depot;
        }

        public void consume(int no) {
            new Thread(() -> depot.consume(no),no+"consume thread").start();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Depot depot = new Depot(500);
        new Product(depot).product(500);
        new Product(depot).product(200);
        new Consumer(depot).consume(500);
        new Consumer(depot).consume(200);
    }
}
