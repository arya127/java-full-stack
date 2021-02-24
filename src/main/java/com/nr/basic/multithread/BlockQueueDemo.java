package com.nr.basic.multithread;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueueDemo {

    public void f1(){
        ConcurrentHashMap map = new ConcurrentHashMap();
        Map hashMap = new HashMap();
        BlockingQueue<Integer> q1 = new ArrayBlockingQueue<>(3);
        q1.offer(1);
        q1.offer(2);
        q1.offer(3);
        q1.offer(4);
        q1.offer(5);
        q1.offer(6);
        q1.poll();
        q1.poll();
        q1.offer(8);
        q1.offer(9);
        System.out.println(Arrays.deepToString(q1.toArray()));
//        Iterator<Integer> iterator = q1.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
    }

    public static void main(String[] args) {
        BlockQueueDemo demo = new BlockQueueDemo();
        demo.f1();
    }
}
