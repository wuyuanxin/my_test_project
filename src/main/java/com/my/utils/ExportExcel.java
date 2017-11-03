package com.my.utils;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * excel导出工具类
 *
 * @author: wuyx
 * @date: 2017/6/23
 * @time: 9:40
 * @see: 链接到其他资源
 * @since: 1.0
 */
public final class ExportExcel {
    private static final Logger logger = LoggerFactory.getLogger(ExportExcel.class);

    /***
     * 工作簿 2003
     */
    private static HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

    /***
     * 工作簿 2007
     */
    private static XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

    /***
     * 工作簿 2003、2007
     */
    private static Workbook workbook;

    /***
     * sheet
     */
    private static Sheet sheet;

    /***
     * 表头行开始位置
     */
    private static final int HEAD_START_POSITION = 0;

    /***
     * 文本行开始位置
     */
    private static final int CONTENT_START_POSITION = 1;

    /**
     * 2003 Excel
     */
    public static final String EXCEL_FORMAT_XLS = ".xls";
    /**
     * 2007 Excel
     */
    public static final String EXCEL_FORMAT_XLSX = ".xlsx";

    /***
     * 构造方法
     */
    private ExportExcel() {
    }

    /**
     * 导出excel调用方法
     *
     * @param bytes    byte数组数据
     * @param fileName 导出的文件名 如：渠道信息表201706231230.xlsx
     * @return
     */
    public static ResponseEntity<byte[]> export(byte[] bytes, String fileName) {
        return new ResponseEntity<byte[]>(bytes, headers(fileName), HttpStatus.OK);
    }

    /**
     * HttpHeaders公共
     *
     * @param fileName 导出的文件名 如：渠道信息表201706231230.xlsx
     * @return
     */
    public static HttpHeaders headers(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Type", "application/vnd.ms-excel");
        } catch (UnsupportedEncodingException e) {
            logger.error("请求头转码异常", e);
        }
        return headers;
    }

    /**
     * @param dataList  对象集合
     * @param titleMap  表头信息（对象属性名称->要显示的标题值)[按顺序添加],
     *                  key为表对象属性名称（取对象属性值用），value为表对象属性中文字符（表头列名）
     * @param sheetName 表单名称
     * @param suffer    excel文件后缀格式
     */
    public static byte[] excelExport(Collection<?> dataList, Map<String, String> titleMap, String sheetName, String suffer) {
        // 初始化workbook
        logger.info("初始化workbook===========>>>startTime=" + System.currentTimeMillis());
        initWorkbook(sheetName, suffer);
        logger.info("初始化workbook===========>>>endTime=" + System.currentTimeMillis());
        // 表头行
        createHeadRow(titleMap);
        // 文本行
        createContentRow(dataList, titleMap);
        // 写入处理结果
        return resultByteArray();
    }

    /***
     *
     * @param sheetName 表单名称
     * @param suffer  文件后缀格式
     */
    private static void initWorkbook(String sheetName, String suffer) {
        try {
            if (EXCEL_FORMAT_XLS.equalsIgnoreCase(suffer)) {
                if (workbook != null) hssfWorkbook = new HSSFWorkbook();
                workbook = hssfWorkbook;
            } else if (EXCEL_FORMAT_XLSX.equalsIgnoreCase(suffer)) {
                if (workbook != null) xssfWorkbook = new XSSFWorkbook();
                workbook = xssfWorkbook;
            }
            if (StringUtils.isEmpty(sheetName)) {
                sheet = workbook.createSheet();
            } else {
                sheet = workbook.createSheet(sheetName);
            }
        } catch (Exception e) {
            logger.error("实例化工作文本对象失败", e);
        }
    }

    /**
     * 创建表头
     *
     * @param titleMap 对象属性名称->表头显示名称
     */
    private static void createHeadRow(Map<String, String> titleMap) {
        // 第1行创建
        Row headRow = sheet.createRow(HEAD_START_POSITION);
        int i = 0;
        for (String entry : titleMap.keySet()) {
            Cell headCell = headRow.createCell(i);
            headCell.setCellValue(titleMap.get(entry));
            i++;
        }
    }

    /**
     * 创建数据内容
     *
     * @param dataList 对象数据集合
     * @param titleMap 表头信息
     */
    private static void createContentRow(Collection<?> dataList, Map<String, String> titleMap) {
        long curr = System.currentTimeMillis();
        logger.info("导出-数据映射开始时间===========>>>startTime=" + curr);
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            int i = 0;
            for (Object obj : dataList) {
                Row textRow = sheet.createRow(CONTENT_START_POSITION + i);
                int j = 0;
                for (String entry : titleMap.keySet()) {
                    String method = "get" + entry.substring(0, 1).toUpperCase() + entry.substring(1);
                    Method m = obj.getClass().getMethod(method);
                    Object o = m.invoke(obj);
                    if (null != o && o instanceof Date) {
                        //o = DateUtil.dateToStr((Date) o, DateConstant.DATE_PATTERN_YMD);TODO
                    }
                    String value = null;
                    if (null != o) {
                        value = o.toString();
                    }
                    Cell textcell = textRow.createCell(j);
                    textcell.setCellValue(value);
                    j++;
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("实例化工作文本对象失败", e);
        }
        long end = System.currentTimeMillis();
        logger.info("导出-数据映射截止时间===========>>>endTime=" + end);
        logger.info("数据映射时间戳（毫秒）==========>>>endTime-startTime=" + (end - curr));
    }

    /**
     * @param dataList  对象集合
     * @param tableName 表头信息（要显示的标题值)[对象属性与表头要按顺序对应]
     * @param sheetName 表单名称
     * @param suffer    excel文件后缀格式
     */
    public static byte[] excelExport(Collection<?> dataList, String[] tableName, String sheetName, String suffer) {
        // 初始化workbook
        initWorkbook(sheetName, suffer);
        // 表头行
        createHeadRow(tableName);
        // 文本行
        createContentRow(dataList, tableName.length);
        // 写入处理结果
        return resultByteArray();
    }

    /**
     * 创建表头
     *
     * @param tableName 表头显示名称
     */
    private static void createHeadRow(String[] tableName) {
        // 第1行创建
        Row row = sheet.createRow(HEAD_START_POSITION);
        for (int i = 0; i < tableName.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(tableName[i]));
        }
    }

    /**
     * 创建数据内容
     *
     * @param dataList  对象数据集合
     * @param titleSize 表头字段总个数
     */
    private static void createContentRow(Collection<?> dataList, int titleSize) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            int i = 0;
            Class clazz = null;
            Field[] fields = null;
            for (Object obj : dataList) {
                Row textRow = sheet.createRow(CONTENT_START_POSITION + i);
                clazz = obj.getClass();
                fields = clazz.getDeclaredFields();
                int j = 0;
                for (Field f : fields) {
                    // 注意：对象属性顺序必须和表头对应，如果取值长度等于了表头长度则停止取值
                    if (j == titleSize) break;
                    Cell textcell = textRow.createCell(j);
                    Method m = new PropertyDescriptor(f.getName(), clazz).getReadMethod();
                    Object o = m.invoke(obj);
                    if (o instanceof Date) {
                        //o = DateUtil.dateToStr((Date) o, DateConstant.DATE_PATTERN_YMD);TODO
                    }
                    textcell.setCellValue(null != o ? o.toString() : null);
                    j++;
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("实例化工作文本对象失败", e);
        }
    }

    /**
     * 处理返回数据的字节流数组
     *
     * @return
     */
    private static byte[] resultByteArray() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            logger.error("数据导出异常", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("实例化工作文本对象失败", e);
                }
            }
        }
        return os.toByteArray();
    }

    /**
     * Annotation 注解方式实现Excel导出
     * @param dataList  对象集合
     * @param clz       实体class对象，获取对象注解，取注解表头名称、排序，并取方法名
     * @param sheetName 表单名称
     * @param suffer    excel文件后缀格式
     */
    public static byte[] excelExport(Collection<?> dataList, Class clz, String sheetName, String suffer) {
        // 初始化workbook
        initWorkbook(sheetName, suffer);
        // 文本行、表头行设值
        createContentRow(dataList, createHeadRow(clz));
        // 写入处理结果
        return resultByteArray();
    }

    /**
     * 创建数据内容
     *
     * @param dataList  对象数据集合
     * @param headers
     */
    private static void createContentRow(Collection<?> dataList, List<ExcelHeader> headers) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            int i = 0;
            for (Object obj : dataList) {
                Row textRow = sheet.createRow(CONTENT_START_POSITION + i);
                int j = 0;
                for (ExcelHeader eh : headers) {
                    Cell textcell = textRow.createCell(j);
                    Method m = eh.getMethod();
                    Object o = m.invoke(obj);
                    if (o instanceof Date) {
                        // o = DateUtil.dateToStr((Date) o, DateConstant.DATE_PATTERN_YMD);
                    }
                    textcell.setCellValue(null != o ? o.toString() : null);
                    j++;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("读取对象内容失败", e);
        }
    }

    /**
     * 创建表头
     * @param clz class对象
     */
    private static List<ExcelHeader> createHeadRow(Class clz) {
        List<ExcelHeader> headers = getHeaderList(clz);
        Collections.sort(headers);
        Row headRow = sheet.createRow(HEAD_START_POSITION);
        int t = 0;
        for (ExcelHeader eh : headers) {
            Cell cell = headRow.createCell(t);
            cell.setCellValue(eh.getTitle());
            t++;
        }
        return headers;
    }

    /**
     * 获取对象注解和方法名
     * @param clz class对象
     * @return
     */
    private static List<ExcelHeader> getHeaderList(Class clz) {
        List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
        Method[] mt = clz.getDeclaredMethods();
        for (Method m : mt) {
            String mn = m.getName();
            if (mn.startsWith("get")) {
                if (m.isAnnotationPresent(ExcelResources.class)) {
                    ExcelResources er = m.getAnnotation(ExcelResources.class);
                    headers.add(new ExcelHeader(er.title(), er.order(), mn, m));
                }
            }
        }
        return headers;
    }

}
