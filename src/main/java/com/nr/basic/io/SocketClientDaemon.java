package com.nr.basic.io;

import java.util.concurrent.CountDownLatch;

/**
 * 客户端代码
 */
public class SocketClientDaemon {

    public static void main(String[] args) {
        Integer clientNumber = 20;
        CountDownLatch countDownLatch = new CountDownLatch(clientNumber);

        for (int i = 0; i < clientNumber; i++, countDownLatch.countDown()) {
            SocketClientRequestThread thread = new SocketClientRequestThread(countDownLatch,i);
            new Thread(thread).start();//模拟多个客户端同时发送请求
        }

        synchronized (SocketClientDaemon.class) {
            try {
                SocketClientDaemon.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
