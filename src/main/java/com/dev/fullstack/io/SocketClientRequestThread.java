package com.dev.fullstack.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * 一个SocketClientRequestThread线程模拟一个客户端请求。
 */
public class SocketClientRequestThread implements Runnable {

    private static final Log LOGGER = LogFactory.getLog(SocketClientRequestThread.class);

    private CountDownLatch countDownLatch;

    /**
     * 这个线层的编号
     *
     * @param countDownLatch
     */
    private Integer clientIndex;

    /**
     * countDownLatch是java提供的同步计数器。
     * 当计数器数值减为0时，所有受其影响而等待的线程将会被激活。这样保证模拟并发请求的真实性
     * @param countDownLatch
     * @param clientIndex
     */
    public SocketClientRequestThread(CountDownLatch countDownLatch, Integer clientIndex) {
        this.countDownLatch = countDownLatch;
        this.clientIndex = clientIndex;
    }

    @Override
    public void run() {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            socket = new Socket("localhost", 8888+clientIndex);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            //等待，直到SocketClientDaemon完成所有线程的启动，然后所有线程一起发送请求
            this.countDownLatch.await();
            out.write(("这是是" + clientIndex + "客户端请求").getBytes());
            out.flush();
            LOGGER.info("发送信息完毕");
            byte[] contextBytes = new byte[1024];
            int length = -1;
            StringBuilder sb = new StringBuilder();
            //等待服务器返回信息
            while ((length = in.read(contextBytes)) != -1) {
                sb.append(new String(contextBytes, "UTF-8"));
            }
            LOGGER.info("接收到服务器的消息位"+sb.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
