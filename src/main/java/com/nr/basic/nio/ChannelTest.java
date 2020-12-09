package com.nr.basic.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {

    public void channel(){
        FileInputStream inputStream = null;
        FileChannel c1 = null;
        try {
            inputStream = new FileInputStream("C:\\work_Project\\1111.txt");
            c1 = inputStream.getChannel();
            //c1.transferFrom();
            ByteBuffer byteBuffer = ByteBuffer.allocate(3);
            int read = c1.read(byteBuffer);
            while (read!=-1){
                System.out.println(read);
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()){
                    System.out.println((char) byteBuffer.get());
                }
                byteBuffer.clear();
                read = c1.read(byteBuffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        ChannelTest test = new ChannelTest();
        test.channel();
    }

}
