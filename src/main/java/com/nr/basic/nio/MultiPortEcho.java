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
        // Create a new selector
        Selector selector = Selector.open();
        // Open a listener on each port, and register each one
        // with the selector
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
                if ((key.readyOps() & SelectionKey.OP_ACCEPT)
                        == SelectionKey.OP_ACCEPT) {
                    // Accept the new connection
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // Add the new connection to the selector
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    it.remove();
                    System.out.println("Got connection from " + sc);
                } else if ((key.readyOps() & SelectionKey.OP_READ)
                        == SelectionKey.OP_READ) {
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer echoBuffer = (ByteBuffer) key.attachment();
                    // Echo data
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
                }
            }
        }
    }
}
