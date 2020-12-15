package com.nr.basic.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MultiPortEcho {
    private static final Log LOGGER = LogFactory.getLog(MultiPortEcho.class);

    private int ports[];
    private static final ConcurrentMap<Integer, StringBuilder> MESSAGEHASHCONTEXT = new ConcurrentHashMap<Integer, StringBuilder>();

    public MultiPortEcho(int ports[]) throws IOException {
        this.ports = ports;
        go();
    }

    static public void main(String args[]) throws Exception {
        int ports[] = {8888, 9999};
        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }
        new MultiPortEcho(ports);
    }

    private void go() throws IOException {
        Selector selector = Selector.open();
        for (int i = 0; i < ports.length; ++i) {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);
            SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Going to listen on " + ports[i]);
        }
        while (true) {
            int num = selector.select();
            if (num == 0) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                if (key.isValid() && key.isAcceptable()) {
                    //在server socket channel接收到/准备好 一个新的 TCP连接后。
                    //就会向程序返回一个新的socketChannel。<br>
                    //但是这个新的socket channel并没有在selector“选择器/代理器”中注册，
                    //所以程序还没法通过selector通知这个socket channel的事件。
                    //于是我们拿到新的socket channel后，要做的第一个事情就是到selector“选择器/代理器”中注册这个
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    //这个已经处理的readyKey一定要移除。如果不移除，就会一直存在在selector.selectedKeys集合中
                    //待到下一次selector.select() > 0时，这个readyKey又会被处理一次
                    it.remove();
                    System.out.println("Got connection from " + sc);
                } else if (key.isValid() && key.isReadable()) {
                    //得到channel
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer echoBuffer = (ByteBuffer) key.attachment();
                    StringBuilder message = new StringBuilder();
                    while (true) {
                        echoBuffer.clear();
                        int r = sc.read(echoBuffer);
                        if (r <= 0) {
                            sc.close();
                            break;
                        }
                        echoBuffer.flip();
                        byte[] messageByte = new byte[echoBuffer.capacity()];
                        echoBuffer.get(messageByte, echoBuffer.position(), r);
                        message.append(new String(messageByte, 0, r, "UTF-8"));
                    }
                    if (URLDecoder.decode(message.toString(), "UTF-8").indexOf("over") != -1) {
                        //则从messageHashContext中，取出之前已经收到的信息，组合成完整的信息
                        Integer channelUUID = sc.hashCode();
                        LOGGER.info("客户端发来的信息======message : " + message);
                        //======================================================
                        //          当然接受完成后，可以在这里正式处理业务了
                        //======================================================

                        //回发数据，并关闭channel
                        ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("回发处理结果,SUCCESS", "UTF-8").getBytes());
                        sc.write(sendBuffer);
                        sc.close();
                    } else {
                        LOGGER.info("客户端信息还未接受完，继续接受======message : " + URLDecoder.decode(message.toString(), "UTF-8"));
                        //每一个channel对象都是独立的，所以可以使用对象的hash值，作为唯一标示
                        Integer channelUUID = sc.hashCode();

                        //然后获取这个channel下以前已经达到的message信息
                        StringBuilder historyMessage = MESSAGEHASHCONTEXT.get(channelUUID);
                        if (historyMessage == null) {
                            historyMessage = new StringBuilder();
                            MESSAGEHASHCONTEXT.put(channelUUID, historyMessage.append(message));
                        }
                        it.remove();
                    }
                    selectedKeys.clear();
                } else if (key.isValid() && key.isWritable()) {

                }
            }
        }
    }
}
