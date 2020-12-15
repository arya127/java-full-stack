package com.nr.basic.io;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio+多线程
 * 服务器代码
 */
public class SocketServer {

    private static final Log LOGGER = LogFactory.getLog(SocketServer.class);

    public void go() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {//服务器线程发起一个accept动作，询问操作系统 是否有新的socket套接字信息从端口X发送过来。一直阻塞等待
                Socket socket = serverSocket.accept();
                SocketServerThread thread = new SocketServerThread(socket);//用线程池优化
                new Thread(thread).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SocketServerThread implements Runnable {
        private final Log logger = LogFactory.getLog(SocketServerThread.class);

        private Socket socket;

        public SocketServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
                byte[] contextBytes = new byte[1024];
                int length = -1;
                StringBuilder sb = new StringBuilder();
                while ((length = in.read(contextBytes)) != -1) {
                    sb.append(new String(contextBytes, "UTF-8"));
                }
                logger.info("客户端发送消息为" + sb.toString());
                //下面开始发送信息
                out.write("回发响应信息！".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //试图关闭
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (this.socket != null) {
                        this.socket.close();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void main(String[] args) {
        new SocketServer().go();
    }
}
