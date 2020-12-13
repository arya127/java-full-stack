package com.nr.basic.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientTest {

    public void Selector(){
        //Selector selector = null;
        int port = 9999;
        try {
            //selector = Selector.open();//打开通道
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1", 8888));
            //SelectionKey sscKey = sc.register(selector, SelectionKey.OP_CONNECT);
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            String str = "New String to write to file..." + System.currentTimeMillis();
            byteBuffer.clear();
            byteBuffer.put(str.getBytes());
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()){
                sc.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientTest test = new ClientTest();
        test.Selector();
    }

}
