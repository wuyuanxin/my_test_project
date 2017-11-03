package com.my.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * jdk8新特性 optional
 * http://www.cnblogs.com/maofa/p/6407046.html
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/5
 * @time: 15:42
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class Jdk8ToOptionalMain {
    public static void main(String[] args) {
        List arr = new ArrayList();
        arr.add("fdsf");
        arr.add("asdf");
        arr.add("abc");
        arr.add("fdac");
        arr.add("asdf");
        arr.add(new String("abc"));
        ArrayList a = new ArrayList();//创建一个新集合
        a.add("abc");//添加要删除的

        //删除所以a里包含的
        arr.removeAll(a);
        System.out.println(arr.toString());

        String d = "1";
        if(d.matches("^[0-9]\\d{0,9}$")){
            System.out.println(1+"\n");
        }

        System.out.println("===================================Optional>>========================================");
        //jdk8新特性
        arr.forEach(e -> {
            System.out.println(e);
        });
        Arrays.asList( "0", "1", "-1" ).sort((e1 , e2) -> {
            int reslut = e1.compareTo(e2);
            System.out.println(reslut+">>"+e1+","+e2+"|||||||||");
            return reslut;
        });

        // of方法通过工厂方法创建Optional类。需要注意的是，创建对象时传入的参数不能为null。如果传入参数为null，则抛出NullPointerException
        Optional<String> name = Optional.of("Sanaulla");
        System.out.println("isPresent>>不空："+name.isPresent());
        System.out.println("get方法取值值为："+name.get());
        name.ifPresent(value -> System.out.println("ifPresent>>如果Optional实例有值则为其调用consumer，否则不做处理："+value));
        Optional<String> uppdername = name.map(v -> v.toUpperCase());
        System.out.println("map方法用来对Optional实例的值执行一系列操作，可以将现有的Opetional实例的值转换成新的optional："+uppdername.orElse("No value found"));
        Optional<String> uppdername2 = uppdername.flatMap(v -> Optional.of(v.toLowerCase()+"------------------------"));
        System.out.println("flatMap调用结束时，flatMap不会对结果用Optional封装,所以传入参数必须是Optional类型："+uppdername2.orElse("No value found"));
        Optional<String> longName = name.filter(value -> value.length() > 7);
        System.out.println("filter>>如果满足条件则返回同一个Option实例，否则返回空Optional："+longName.isPresent());

        //ofNullable为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional，ofNullable与of方法相似，唯一的区别是可以接受参数为null的情况
        Optional<String> optional = Optional.ofNullable(null);
        System.out.println("isPresent>>为空："+optional.isPresent());
        System.out.println("如果为null则取一个默认值（orElseGet方法可以接受Supplier接口的实现用来生成默认值）：" + optional.orElseGet(() ->"[none]"));
        System.out.println("如果为null则返回传入的值（orElse方法将传入的字符串作为默认值）：" + optional.orElse("none"));
        System.out.println("map()方法可以将现有的Opetional实例的值转换成新的optional："+optional.map(e -> "hi,"+e+"！"));
        System.out.println("新的optional值："+optional.map(e -> "hi,"+e+"！").orElse("Hi,Nothing！"));
        try {
            optional.orElseThrow(Exception :: new);
        } catch (Exception e) {
            System.out.println("orElseThrow>>如果值不存在抛出异常");
            e.printStackTrace();
        }
    }
}
