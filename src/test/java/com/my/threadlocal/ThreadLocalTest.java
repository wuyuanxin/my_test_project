package com.my.threadlocal;

/**
 * ThreadLocal
 * http://blog.csdn.net/sonny543/article/details/51336457
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/8
 * @time: 13:52
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class ThreadLocalTest {
    public final static ThreadLocal<String> TEST_THREAD_NAME_LOCAL = new ThreadLocal<String>();
    public final static ThreadLocal<String> TEST_THREAD_VALUE_LOCAL = new ThreadLocal<String>();

    final static ThreadLocal<Long> longLocal = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return Thread.currentThread().getId();
        }
    };
    final static ThreadLocal<String> stringLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return Thread.currentThread().getName();
        }
    };

    public ThreadLocalTest() {
        /*longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());*/
    }

    public long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalTest test = new ThreadLocalTest();
        System.out.println(test.getLong());
        System.out.println(test.getString());
        try {
            Thread thread1 = new Thread() {
                public void run() {
                    System.out.println(test.getLong() + "========================");
                    System.out.println(test.getString() + "--------------------------");
                }
            };
            thread1.start();
            thread1.join();
        } finally {
            longLocal.remove();
            stringLocal.remove();
        }
        System.out.println(test.getLong());
        System.out.println(test.getString());


        System.out.println("\n-----------------------------------------------------------------------------");
        for(int i = 0 ; i < 100 ; i++) {
            final String name = "线程-【" + i + "】";
            final String value =  String.valueOf(i);
            Thread td = new Thread() {
                public void run() {
                    try {
                        TEST_THREAD_NAME_LOCAL.set(name);
                        TEST_THREAD_VALUE_LOCAL.set(value);
                        callA();
                    } finally {
                        TEST_THREAD_NAME_LOCAL.remove();
                        TEST_THREAD_VALUE_LOCAL.remove();
                    }
                }
            };
            td.start();
            td.join();
        }
        System.out.println(TEST_THREAD_NAME_LOCAL.get()+"================================");

    }

    public static void callA() {
        callB();
    }

    public static void callB() {
        new ThreadLocalTest().callC();
    }

    public void callC() {
        callD();
    }

    public void callD() {
        System.out.println(TEST_THREAD_NAME_LOCAL.get() + "/t=" + TEST_THREAD_VALUE_LOCAL.get());
    }
}
