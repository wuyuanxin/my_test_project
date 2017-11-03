package com.my.utils;

import java.lang.reflect.Method;

/**
 * Excel帮助类
 * 表头信息用于自动生成表头结构及排序
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/7/21
 * @time: 10:33
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class ExcelHeader implements Comparable<ExcelHeader> {
    /**
     * excel的标题名称
     */
    private String title;
    /**
     * 每一个标题的顺序
     */
    private int order;
    /**
     * 对应方法名称
     */
    private String methodName;

    /**
     * 对应对象方法
     */
    private Method method;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public int compareTo(ExcelHeader o) {
        return order > o.order ? 1 : (order < o.order ? -1 : 0);
    }

    public ExcelHeader(String title, int order, String methodName, Method method) {
        super();
        this.title = title;
        this.order = order;
        this.methodName = methodName;
        this.method = method;
    }
}
