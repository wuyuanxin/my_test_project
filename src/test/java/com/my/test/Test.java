package com.my.test;

import java.util.Random;

/**
 * 类注释
 *
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/9
 * @time: 15:19
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class Test {
    public static void main(String[] args) {
        String str="YGH08000002";
        String version = str.substring("YGH".length());
        System.out.println(version);
        String result = String.format("%0" + version.length() + "d", Integer.parseInt(version) + 1);
        /*System.out.println(result);
        int size = 4-result.length();
        for(int j=0;j<size;j++){
            result="0"+result;
        }*/
        //System.out.println(result);
        System.out.println(("YGH")+result);


        System.out.println(String.format("%010d", Long.valueOf(getRandomNum())));
    }

    private static String getRandomNum()
    {
        int max=99999999;
        int min=1;
        Random random = new Random();
        int randomNum1 = random.nextInt(99);
        int randomNum2 = random.nextInt(max)%(max-min+1) + min;
        System.out.println(randomNum1 + "--");
        System.out.println("=======" + randomNum1 + "" + randomNum2);
        return randomNum1 + "" + randomNum2;
    }
}
