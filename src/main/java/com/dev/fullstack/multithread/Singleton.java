package com.dev.fullstack.multithread;

public class Singleton {

    public static volatile Singleton singleton;

    private static byte[] bytes = new byte[1024];
    /**
     * 构造函数私有，禁止外部实例化
     */
    private Singleton() {};
    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (bytes) {
                if (singleton == null) {
                    System.out.println(Thread.currentThread().getName()+"进入线程");
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        for (int i=0;i<100;i++){
            new Thread(() ->{
                System.out.println(Thread.currentThread().getName());
                Singleton.getInstance();
            }).start();
        }
    }
}
