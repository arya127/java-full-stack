package com.dev;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MainTest {

    @Test
    public void f1(){
        //System.out.println(Integer.toBinaryString(-8));
        Assert.assertTrue("success",1<2);
        Assert.assertNotNull("不能为null",null);
    }

    @Test(expected = NullPointerException.class)
    public void test2(){
        Object obj = null;
        obj.toString();
    }

    //mock对象的属性如果没有设定值的话，会返回属性的默认值
    @Test
    public void test3(){
        Person p = Mockito.mock(Person.class);
        Person p2 = Mockito.spy(Person.class);
        Mockito.when(p.getStatus()).thenReturn("success");
        Mockito.when(p.getStatus()).thenThrow(NullPointerException.class);//对待有返回值的用thenThrow，void用doThrow
   }

    static class Person{
        private String name;
        private Integer age;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getStatus(){
            return "success";
        }
    }
}
