package com.dev.fullstack.java8;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @version
 * @author nirui
 *
 */
public class DateDemo {

    private static final int DATA_EXPIRE_TIME = 3 * 24 * 3600;

    public void time(){
        Clock clock = Clock.systemUTC();
//        System.out.println(System.currentTimeMillis());
//        System.out.println(clock.millis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dt1 = LocalDateTime.now();
        LocalDateTime dt2 = LocalDateTime.parse("2013-10-20 23:59:59",formatter);
//        System.out.println(dt1);
//        System.out.println(dt2);
//        System.out.println(dt2.getYear());
//        System.out.println(dt2.getMonth());
//        System.out.println(dt2.getMonthValue());
//        System.out.println(dt2.getDayOfYear());k
//        System.out.println(dt2.getDayOfMonth());
//        System.out.println(dt2.getDayOfWeek());
//        System.out.println(dt2.getHour());
//        System.out.println(dt2.getMinute());
//        System.out.println(dt2.getSecond());
//        System.out.println(dt2.getNano());
        LocalDateTime dt3 = dt2.minusDays(1);
        System.out.println(dt3);
        LocalDate d1 = LocalDate.now();
        System.out.println(d1);
    }

    public void date(){
        String s1 = new String("aaa");
        String s2 = new String("aaa");
        System.out.println(s1 == s2);           // false
        String s3 = s1.intern();
        System.out.println(s1.intern() == s3);
        //System.out.printf(DateFormatUtils.format(DateUtils.addSeconds(new Date(), DATA_EXPIRE_TIME * -1),"yyyy-MM-dd HH:mm:ss"));
    }


    public void testParallel(){
        long t0 = System.nanoTime();
        //初始化一个范围100万整数流,求能被2整除的数字，toArray()是终点方法
        int a[]= IntStream.range(0, 100000000).filter(p -> p % 2==0).toArray();
        long t1 = System.nanoTime();
        //和上面功能一样，这里是用并行流来计算
        int b[]=IntStream.range(0, 100000000).parallel().filter(p -> p % 2==0).toArray();
        long t2 = System.nanoTime();
        //我本机的结果是serial: 0.06s, parallel 0.02s，证明并行流确实比顺序流快
        System.out.printf("serial: %.2fs, parallel %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9);
    }

    public void testStream(){
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        String str = "2222";
        Optional<String> longest = stream.reduce((s1, s2) -> {
            System.out.println("one="+s1+","+"two="+s2);
            return s1.length() >= s2.length() ? s1 : s2;
        });

        Stream<String> sumStream = Stream.of("I", "love", "you", "too");
        Integer lengthSum = sumStream.reduce(0, new BiFunction<Integer, String, Integer>() {
            @Override
            public Integer apply(Integer sum, String s) {
                return sum + s.length();
            }
        }, (a, b) -> a+b);
        // 部分和拼接器，并行执行时才会用到 // (3)

        Stream<String> s3 = Stream.of("I", "love", "you", "too");
        List<String> list = s3.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static void main(String[] args) {
        DateDemo dateDemo = new DateDemo();
        //dateDemo.time();
        //dateDemo.testParallel();
        dateDemo.date();
    }
}
