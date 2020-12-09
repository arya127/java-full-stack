package com.nr.basic.io;

import java.io.UnsupportedEncodingException;

public class FileStream {

    public void toBytes(){
        String str = "我的世界";
        byte[] b = null;
        char[] c = null;
        try {
            b = str.getBytes("UTF-8");
            c = str.toCharArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        for (byte b1 : b) {
//            System.out.println(b1);
//        }
        for (char c1 : c) {
            System.out.println(c1);
        }
    }

    public static void main(String[] args) {
        FileStream fileStream = new FileStream();
        fileStream.toBytes();
    }
}
