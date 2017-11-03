package com.my.okhttp;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okio.BufferedSink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * OkHttp练习，学习网址：
 * https://www.ibm.com/developerworks/cn/java/j-lo-okhttp/index.html
 * http://blog.csdn.net/android_freshman/article/details/51910937
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/9
 * @time: 15:19
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class OkHttpTest {
    public static void main(String[] args) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://www.google.cn/")
                .header("User-Agent", "My super agent").addHeader("Accept", "text/html").addHeader("Accept", "text/plain")
                .get().build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException("服务器端错误: " + response);
            System.out.print("headers>>\n");
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i)+"==============================");
            }
            System.out.println("\nbody>>\n"+response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("1。当请求内容较大时，应该使用流来提交---------------------------------------------------------------------------------------------------------------------------");
        String st = "<!DOCTYPE html>\n" +
                "<html lang=\"zh\">\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <title>Google</title>\n" +
                "  <style>\n" +
                "    html { background: #fff; margin: 0 1em; }\n" +
                "    body { font: .8125em/1.5 arial, sans-serif; text-align: center; }\n" +
                "    h1 { font-size: 1.5em; font-weight: normal; margin: 1em 0 0; }\n" +
                "    p#footer { color: #767676; font-size: .77em; }\n" +
                "    p#footer a { background: url(//www.google.cn/intl/zh-CN_cn/images/cn_icp.gif) top right no-repeat; padding: 5px 20px 5px 0; }\n" +
                "    ul { margin: 2em; padding: 0; }\n" +
                "    li { display: inline; padding: 0 2em; }\n" +
                "    div { -moz-border-radius: 20px; -webkit-border-radius: 20px; border: 1px solid #ccc; border-radius: 20px; margin: 2em auto 1em; max-width: 650px; min-width: 544px; }\n" +
                "    div:hover, div:hover * { cursor: pointer; }\n" +
                "    div:hover { border-color: #999; }\n" +
                "    div p { margin: .5em 0 1.5em; }\n" +
                "    img { border: 0; }\n" +
                "  </style>\n" +
                "  </script>";
        OkHttpClient okclent = new OkHttpClient();
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/plain");
            }
            @Override
            public long contentLength() throws IOException {
                return st.length();
            }
            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(st);
            }
        };
        Request request1 = new Request.Builder()
                .url("https://www.google.cn")
                .post(requestBody)
                .build();
        Response response1 = okclent.newCall(request1).execute();
        if(!response1.isSuccessful()) {
            throw new IOException("服务器端错误: " + response1);
        }
        System.out.println(response1.body().string());
        response1.close();
        System.out.println("2。文件上传---------------------------------------------------------------------------------------------------------------------------");

        OkHttpClient okclent2 = new OkHttpClient();
        /**
         * addPart 就是发送头部的具体内容了，其中 addFormDataPart 封装了部分内容，本质上 和
         .addPart(
         Headers.of(“Content-Disposition”, “form-data; name=\”token\”“),
         RequestBody.create(null, uploadToken))

         道理是一样的，只是内部进行了自己的封装
         */
        RequestBody requestBody1 = new  MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addPart(
//                Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"商品_20170605193534.xls\""),
//                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new File("C:\\Users\\Administrator\\Desktop\\商品_20170605193534.xls")))
            .addFormDataPart("file", "商品_20170605193534.xls", RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new File("C:\\Users\\Administrator\\Desktop\\商品_20170605193534.xls")))
            .build();
        Request request2 = new Request.Builder()
                .url("http://127.0.0.1:9005/goodsManage/import")
                .header("x-sljr-session-token", "f2ff9d9b0aaf600b787d67b3ebce0073")
                .post(requestBody1)
                .build();
        Response response2 = okclent2.newCall(request2).execute();
        if(!response2.isSuccessful()) {
            throw new IOException("服务器端错误: " + response2);
        }
        System.out.println(response2.body().string());
        response2.close();

        System.out.println("\n3。Json格式数据传输---------------------------------------------------------------------------------------------------------------------------");
        OkHttpClient okHttpClient1 = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("barCodes", new String[]{"222222,333333"});
        System.out.println("参数json串值："+jsonObject.toJSONString());
        RequestBody rb = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request3 = new Request.Builder()
                .url("http://127.0.0.1:9005/goodsManage/failure")
                .header("x-sljr-session-token", "f2ff9d9b0aaf600b787d67b3ebce0073")
                .post(rb)
                .build();
        Response response3 = okHttpClient1.newCall(request3).execute();
        if(!response3.isSuccessful()) {
            throw new IOException("服务器端错误: " + request3);
        }
        System.out.println(response3.body().string());
        response3.close();

        System.out.println("\n3。异步调用---------------------------------------------------------------------------------------------------------------------------");
        OkHttpClient okclent3 = new OkHttpClient();
        JSONObject jsonObject1 = new JSONObject();
        List<UpdateStock> stockList = new ArrayList<>();
        stockList.add(new UpdateStock("66666666", "333333", 1000));
        stockList.add(new UpdateStock("0002", "666666", 1000));
        jsonObject1.put("terminalStockDto", stockList.toArray());
        System.out.println(jsonObject1.toString());
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json"), jsonObject1.toString());
        Request request5 = new Request.Builder().url("http://127.0.0.1:9005/goodsManage/batchUpdateStock")
                .header("x-sljr-session-token", "f2ff9d9b0aaf600b787d67b3ebce0073")
                .post(requestBody3).build();
        okclent3.newCall(request5).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("操作异常：" + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("服务器端错误: " + response);
                }
                System.out.println(response.body().string());
                response.close();
            }
        });

        System.out.println("\n4。导出数据---------------------------------------------------------------------------------------------------------------------------");
        OkHttpClient okHttpClient2 = new OkHttpClient();
        RequestBody requestBody2 = new FormBody.Builder().add("token", "f2ff9d9b0aaf600b787d67b3ebce0073").build();
        Request request4 = new Request.Builder()
                .url("http://127.0.0.1:9005/goodsManage/export")
                .post(requestBody2)
                .build();
        Response response = okHttpClient2.newCall(request4).execute();
        if(!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }
        System.out.println(response.body().string());
        response.close();

    }
}
