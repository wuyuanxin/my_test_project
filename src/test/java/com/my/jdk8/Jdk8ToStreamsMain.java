package com.my.jdk8;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * jdk8新特性 stream
 * http://www.cnblogs.com/maofa/p/6407046.html
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/5
 * @time: 15:45
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class Jdk8ToStreamsMain {
    private enum Status{
        OPEN,CLOSED
    }

    private static final class Task {
        private final Status status;
        private final Integer points;
        Task(final Status status, final Integer points) {
            this.status = status;
            this.points = points;
        }

        public Integer getPoints() {
            return points;
        }
        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format("[%s, %d]", status, points);
        }
    }

    public static void main(String[] args) {
        final Collection< Task > tasks = Arrays.asList(
                new Task( Status.OPEN, 5 ),
                new Task( Status.OPEN, 13 ),
                new Task( Status.CLOSED, 8 )
        );
        System.out.println("tasks集合被转换成steam表示，stream创建方式还有如：\nArrays.stream(Object[])方法, 比如Arrays.stream(new int[]{1,2,3})或使用流的静态方法，比如Stream.of(Object[])等");
        int sum = tasks.stream().filter(task -> task.getStatus() == Status.OPEN).mapToInt(Task :: getPoints).sum();
        System.out.println("mapToInt操作基于每个task实例的Task::getPoints方法将task流转换成Integer集合(IntStream(Stream接口还包含几个基本类型的子接口如IntStream, LongStream 和 DoubleStream))；最后，通过sum方法计算总和："+sum);
        System.out.println("常见流的中间操作：");
        List<String> collect = Stream.of("a", "b", "c", "d", "b", "e").distinct().collect(Collectors.toList());
        System.out.println("distinct 保证输出的流中包含唯一的元素(不重复)，它是通过Object.equals(Object)来检查是否包含相同的元素："+collect);

        List<Task> collect1 = tasks.stream().filter(task -> task.getStatus() == Status.OPEN).collect(Collectors.toList());
        System.out.println("filter 返回流中只包含满足断言(predicate)的数据："+collect1);
        List<Integer> collect2 = Stream.of("a", "b", "c").map(c -> c.hashCode()).collect(Collectors.toList());
        System.out.println("map 将流中的元素映射成另外的值，新的值类型可以和原来的元素的类型不同(将字符元素a,b,c映射成它的哈希码(ASCII值))："+collect2);

        String poetry = "Where, before me, are the ages that have gone?/n" + "And where, behind me, are the coming generations?/n" + "I think of heaven and earth, without limit, without end,/n" + "And I am all alone and my tears fall down.";
        Stream<String> lines = Arrays.stream(poetry.split("/n"));
        Stream<String> words = lines.flatMap(line -> Arrays.stream(line.split(" ")));
        List<String> l = words.map(w ->{
            if(w.endsWith(",") || w.endsWith(".") || w.endsWith("?")) {
                return w.substring(0, w.length() - 1).trim().toLowerCase();
            }else{
                return w.trim().toLowerCase();
            }
        }).distinct().sorted().collect(Collectors.toList());
        System.out.println("flatmap 将映射后的流的元素全部放入到一个新的流中(方法distinct().sorted()去除重复项并按照字母排序)："+l);

        List<String> limit = Arrays.stream(poetry.split("/n"))
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .limit(10).collect(Collectors.toList());
        System.out.println("limit 方法指定数量的元素的流。对于串行流，这个方法是有效的，这是因为它只需返回前n个元素即可，但是对于有序的并行流，它可能花费相对较长的时间，如果你不在意有序，可以将有序并行流转换为无序的，可以提高性能。："+limit);
        List<String> skip = Arrays.stream(poetry.split("/n"))
                .flatMap(line -> Arrays.stream(line.split(" "))).skip(27).collect(Collectors.toList());
        System.out.println("skip 返回丢弃了前n个元素的流，如果流中的元素小于或者等于n，则返回空的流："+skip.toString());
        String[] s = {"b_123", "c+342", "b#632", "d_123"};
        List<String> col = Arrays.stream(s).sorted((s1,s2) ->{
            if(s1.charAt(0)==s2.charAt(0)){
                return s1.compareTo(s2);
            } else {
                return s1.charAt(0)-s2.charAt(0);
            }
        }).collect(Collectors.toList());
        System.out.println("sorted 将流中的元素按照指定排序方式进行排序，对于有序流，排序是稳定的。对于非有序流，不保证排序稳定："+col.toString());

        System.out.println("peek 方法会使用一个Consumer消费流中的元素，但是返回的流还是包含原来的流中的元素："+Arrays.asList("a", "b", "c", "d").stream().peek(s1 -> System.out.println(s1.toLowerCase())).collect(Collectors.toList()).toString());

        System.out.println("\n终点操作：");
        System.out.println("count 方法返回流中的元素的数量："+Arrays.stream(poetry.split("/n")).count());
        System.out.println("findFirst 有可能多次执行的时候返回的结果不一样。findFirst()返回第一个元素，如果流为空，返回空的Optional（findAny()返回任意一个元素，如果流为空，返回空的Optional）："+Arrays.stream(poetry.split("/n")).findFirst());
        System.out.println("max 返回流中的最大值，min 返回流中的最小值："+Arrays.stream(new String[]{"a", "b", "c"}).max((s1,s2) ->{
            if(s1.charAt(0)==s2.charAt(0)){
                return s1.compareTo(s2);
            } else {
                return s1.charAt(0)-s2.charAt(0);
            }
        }));
        Arrays.stream(s).sorted((s1,s2) ->{
            if(s1.charAt(0)==s2.charAt(0)){
                return s1.compareTo(s2);
            } else {
                return s1.charAt(0)-s2.charAt(0);
            }
        }).forEachOrdered(orders -> System.out.println("forEach遍历流的每一个元素，执行指定的action。它是一个终点操作，和peek方法不同，这个方法不担保按照流的encounter order顺序执行（如果对于有序流按照它的encounter order顺序执行，你可以使用forEachOrdered方法）："+orders));

    }
}
