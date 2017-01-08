package com.dept.web.general.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 
 * @author xiaofeng
 * @param <T>此处应用泛型，代表任意一个符合javabean风格的类
 *          
 */
@SuppressWarnings("all")
public class ExportExcel<T> {

//    public void exportExcel(Collection<T> dataset, OutputStream out) {
//        exportExcel("导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");
//    }

    public void exportExcel(String[] headers, Collection<T> dataset,
            OutputStream out,String[] names) {
        exportExcel("导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd",names);
    }

// 

    /**
     * 利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     * 
     * @param title
     *            表格标题名
     * @param headers
     *            表格属性列名数组
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out
     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern
     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
    public void exportExcel(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern,String[] names) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 设置标题样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个标题字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        
        
        // 生成内容样式
        HSSFCellStyle cell_style = workbook.createCellStyle();
        cell_style.setFillForegroundColor(HSSFColor.WHITE.index);
        cell_style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cell_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cell_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cell_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cell_style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setFontHeightInPoints((short) 10);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        cell_style.setFont(font2);
        
        int sheetCount = 1000;
        if (dataset.size() > sheetCount) {
            Iterator<T> it = dataset.iterator();
            for (int i = 0; i <= 4; i++) {
                int index = 0;
                List<T> list = new ArrayList<T>();
                while (it.hasNext()) {
                    index++;
                    if (index < sheetCount) {
                        list.add(it.next());
                    } else {
                        break;
                    }
                }
                generateSheet(list, style, cell_style, workbook, pattern, headers,
                        title + "_" + (i + 1),names);
            }
        } else {
            generateSheet(dataset, style, cell_style, workbook, pattern, headers,
                    title,names);
        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void generateSheet(Collection<T> dataset, HSSFCellStyle style,
            HSSFCellStyle style2, HSSFWorkbook workbook, String pattern,
            String[] headers, String title,String[]names) {
       
        HSSFSheet sheet = workbook.createSheet(title); // 生成一个表格
        sheet.setDefaultColumnWidth(20);// 设置表格默认列宽度为15个字节
        
        
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();// 声明一个画图的顶级管理器
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));    // 定义注释的大小和位置
        comment.setString(new HSSFRichTextString("")); // 设置注释内容
        comment.setAuthor("author");// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.

        HSSFRow row = sheet.createRow(0);// 产生一行
		// 产生单元格
	    row.setHeight((short)400);
//		 写入各个字段的名称
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell;
			cell = row.createCell(i); // 创建第一行各个字段名称的单元格
			cell.setCellStyle(style);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置单元格内容为字符串型
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			// //为了能在单元格中输入中文,设置字符集为UTF_16
			 HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text); // 给单元格内容赋值
		}
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < names.length; i++) {
//                Field field = fields[i];
//                if (field.toString().contains("static")) {
//                    continue;
//                }
                HSSFCell cell =row.createCell(i);
                cell.setCellStyle(style2);
               String getMethodName= "get"+ names[i].substring(0, 1).toUpperCase()
                       + names[i].substring(1);
                try {
                	
                    Class tCls = t.getClass();
                 
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[] {});
                    Object value = getMethod.invoke(t, new Object[] {});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        textValue = "男";
                        if (!bValue) {
                            textValue = "女";
                        }
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof byte[]) {
                        row.setHeightInPoints(60); // 有图片时，设置行高为60px;
                        row.setHeight((short)400); // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                1023, 255, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(2);
                        patriarch.createPicture(anchor, workbook.addPicture(
                                bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }else {
                        // 其它数据类型都当作字符串简单处理
                        if (value == null) {
                            value = "";
                        }
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?{1}quot;");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            cell.setCellValue(Double.parseDouble(textValue)); // 是数字当作double处理
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(
                                    textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLACK.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                      
                    }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }

        }
    }

}