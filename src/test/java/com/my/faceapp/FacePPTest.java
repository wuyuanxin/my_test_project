package com.my.faceapp;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 类注释
 *
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/13
 * @time: 10:00
 * @see: 链接到其他资源
 * @since: 1.0
 */
public class FacePPTest {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        File file = new File("D:\\222.png");
        byte[] buff = FacePPHttpRequests.getRequests().getBytesFromFile(file);
        // 一个图片，二进制文件
        byteMap.put("image_file", buff);
        map.put("api_key", "3RHCB81fxLrZpGi6a6oHn7IZW3HV9qsH");
        map.put("api_secret", "4xhuv3T5jn1C2RFv2By80hZSSfbcTymC");
        // 是否检测并返回人脸五官和轮廓的83个关键点(1:检测，0:不检测，默认为0)
        map.put("return_landmark", 1);
        // 是否检测并返回根据人脸特征判断出的年龄，性别，微笑、人脸质量等属性，需要将需要检测的属性组织成一个用逗号分隔的字符串。默认值为 none ，表示不检测属性
        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,ethnicity");
        try {
            byte[] bytes = FacePPHttpRequests.getRequests().post(url, map, byteMap);
            getFaceData(bytes, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getFaceData(byte[] bytes, File file) throws IOException {
        String str = new String(bytes);
        System.out.println(str);
        JSONObject result = new JSONObject(str);
        JSONArray array1 = result.getJSONArray("faces");
        if (array1.length() < 1) {
            System.out.println("图像识别失败......");
            return;
        }
        // 获取人脸矩形框，坐标数字为整数，代表像素点坐标
        JSONObject face_rectangle = array1.getJSONObject(0).getJSONObject("face_rectangle");
        int top = face_rectangle.getInt("top");// 左上角纵坐标
        int left = face_rectangle.getInt("left");// 左上角横坐标
        int height = face_rectangle.getInt("height");// 高度
        int width = face_rectangle.getInt("width");// 宽度

        // 获取人脸识别信息（如性别、年龄等）
        JSONObject attributes = array1.getJSONObject(0).getJSONObject("attributes");
        int age = attributes.getJSONObject("age").getInt("value");
        String gender = attributes.getJSONObject("gender").getString("value");
        String ethnicity = attributes.getJSONObject("ethnicity").getString("value");
        double smiles = attributes.getJSONObject("smile").getDouble("value");

        // 人脸信息转换
        if (gender.equals("Male"))
            gender = "男";
        else
            gender = "女";
        switch (ethnicity) {
            case "Asian":
                ethnicity = "黄种人";
                break;
            case "White":
                ethnicity = "白种人";
                break;
            case "Black":
                ethnicity = "黑种人";
                break;
            default:
                break;
        }
        System.out.println("年龄:" + age + "\n" + "性别:" + gender + "\n" + "肤色:" + ethnicity + "\n" + "表情:" + smiles);

        BufferedImage buffImg = ImageIO.read(file);
        Graphics graphics = buffImg.getGraphics();
        graphics.setColor(Color.GREEN);
        graphics.drawRect(left, top, width, height);

		/*graphics.setColor(Color.pink);
		graphics.fillRect(width + left + 5, height + top - 65, width + left + 5, 65);
		graphics.setColor(Color.red);
		// 第一个值是你设置的内容，10,20表示这段文字在图片上的位置(x,y)
		graphics.drawString("性别：" + gender, width + left + 5, height + top -50);
		graphics.drawString("年龄：" + age, width + left + 5, height + top - 35);
		graphics.drawString("肤色：" + ethnicity, width + left + 5, height + top - 20);
		graphics.drawString("表情：" + smiles, width + left + 5, height + top - 5);*/

        graphics.setColor(Color.pink);
        graphics.fillRect(buffImg.getWidth()-100, buffImg.getHeight()-65, buffImg.getWidth()-100, buffImg.getHeight()-65);
        graphics.setColor(Color.red);
        graphics.drawString("性别：" + gender, buffImg.getWidth()-100, buffImg.getHeight()-50);
        graphics.drawString("年龄：" + age, buffImg.getWidth()-100, buffImg.getHeight()-35);
        graphics.drawString("肤色：" + ethnicity, buffImg.getWidth()-100, buffImg.getHeight()-20);
        graphics.drawString("表情：" + smiles, buffImg.getWidth()-100, buffImg.getHeight()-5);

        graphics.dispose();

        ImageIO.write(buffImg, "jpg", new File("E:\\" + file.getName()));
        System.out.println("图片已生成......");
    }
}
