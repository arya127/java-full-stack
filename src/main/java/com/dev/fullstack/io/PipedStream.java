package com.dev.fullstack.io;

import java.io.*;

public class PipedStream {

    public void byteArrayStream() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("This text is converted to bytes".getBytes("UTF-8"));
        byte[] bytes = output.toByteArray();
    }

    public static void main(String[] args) {
        try(PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream inputStream = new PipedInputStream(outputStream)) {
            Thread one = new Thread(() -> {
                try {
                    outputStream.write("hello world".getBytes());
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Thread two = new Thread(() -> {
                try {
                    int data = -1;
                    while ((data = (inputStream.read()))!=-1){
                        System.out.println((char)data);
                    }
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            one.start();
            two.start();
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
