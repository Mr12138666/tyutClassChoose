package com.sunrisejay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunrisejay.constant.Constant;
import com.sunrisejay.pojo.Course;
import com.sunrisejay.pojo.User;
import com.sunrisejay.util.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther Sunrise_Jay
 */

public class CheckLessonService {
    public void getLesson(User user) throws Exception {


        //请求体构造
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("zxjxjhh", ""));


        String content = HttpClientUtils.doPost("Tresources/A1Xskb/GetXsKb", Constant.NODE, user.getCookieStore(), formParams);

        showLesson(content);

    }


    private void showLesson(String content) throws IOException {

        JSONObject jsonObject = JSONObject.parseObject(content);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        List<Course> courseList = jsonArray.toJavaList(Course.class);
        Map<String[], String> courseMap = new HashMap<>();
        //拼接课表单元信息并与key:SKxq+Jc形成映射
        for (Course course : courseList) {
            if (course.getSkxq() == null || course.getJc() == null) {
                continue;
            }
            String[] SkxqAndJc = new String[2];
            SkxqAndJc[0] = course.getSkxq().toString();
            SkxqAndJc[1] = course.getJc().substring(0, 1);
            courseMap.put(SkxqAndJc, course.getKcm() + "\n" + course.getZcsm() + "\n" + course.getDd());
        }
        FileInputStream fileIn = new FileInputStream(Constant.EXPORT_PATH);
        //获得工作表
        XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
        //获得所在sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = null;
        for (Map.Entry<String[], String> entry : courseMap.entrySet()) {
            row = sheet.getRow(Integer.parseInt(entry.getKey()[1]) * 2 - 1);
            row.getCell(Integer.parseInt(entry.getKey()[0]) * 2).setCellValue(entry.getValue());
        }
        //写回
        FileOutputStream fileOut = new FileOutputStream(Constant.EXPORT_PATH);
        workbook.write(fileOut);
        workbook.close();
        fileIn.close();
        fileOut.close();
        System.out.println("已成功写出表格，请检查：src\\main\\resources\\Course.xlsx");



    }


}
