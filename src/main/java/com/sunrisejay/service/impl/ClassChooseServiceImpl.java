package com.sunrisejay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunrisejay.pojo.ClassChooseData;
import com.sunrisejay.pojo.TeacherChooseData;
import com.sunrisejay.pojo.User;
import com.sunrisejay.service.ClassChooseService;
import com.sunrisejay.utils.HttpClientUtil;
import com.sunrisejay.utils.RsaUtil;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sunrisejay.constant.Constant.NODE;
import static com.sunrisejay.constant.Constant.XNXQ;

/**
 * @auther Sunrise_Jay
 */

public class ClassChooseServiceImpl implements ClassChooseService {

    public String getCourseList(User user) throws Exception {

        //请求体构造
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("limit", "30"));
        formParams.add(new BasicNameValuePair("offset", "0"));
        formParams.add(new BasicNameValuePair("sort", "xh"));
        formParams.add(new BasicNameValuePair("order", "asc"));

        //todo 需要改进学年选择
        formParams.add(new BasicNameValuePair("conditionJson", "{\"zxjxjhh\":\"2024-2025-1-1\"}"));


        String content = HttpClientUtil.doPost("Tschedule/C4Xkgl/GetXkPageListJson", NODE, user.getCookieStore(), formParams);

        return content;
    }

    public String getFirstCourseUID(User user, String courseList) {

        JSONObject jsonObject = JSONObject.parseObject(courseList);

        //此处获取总共带选课数目,一般为一个，如果不为1，就反复请求直到获取到
        int total = jsonObject.getInteger("total");

        if (total == 0) {
            System.out.println("暂无选课信息");
            return null;
        }

        //走到这就说明有课可以选，这里我们默认只有一个，所以只需要获取第一个课程的UID就可以了
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        List<ClassChooseData> dataObjects = jsonArray.toJavaList(ClassChooseData.class);

        //todo 但是为了未来优化，建议遍历所有课程，选择最优课程
        String uid = dataObjects.get(0).getId();
        System.out.println("获取到课程UID" + uid);

        return uid;
    }

    public String getCourseDetail(User user, String uid) throws Exception {

        //构建请求参数之一
        String conditionJson = "{\"zxjxjhh\":\"" + XNXQ + "\",\"kch\":\"\",\"pid\":\"" + uid + "\"}";

        //请求体构造
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("limit", "150"));
        formParams.add(new BasicNameValuePair("offset", "0"));
        formParams.add(new BasicNameValuePair("sort", "kch,kxh"));
        formParams.add(new BasicNameValuePair("order", "asc"));
        formParams.add(new BasicNameValuePair("conditionJson", conditionJson));

        //todo 此地址存疑！！！！！
        String content = HttpClientUtil.doPost("Tschedule/C4Xkgl/GetXkkcListByXh", NODE, user.getCookieStore(), formParams);

        return content;

    }

    public Boolean tryCatchTeacher(User user, String courseDetail, String pid, List<TeacherChooseData> dataObjects) throws Exception {


        //筛选出未选满老师列表
        ArrayList<String> teachers = new ArrayList<>();
        for (TeacherChooseData dataObject : dataObjects) {
            if (dataObject.getYbrs().intValue() >= dataObject.getBkskrl().intValue()) {

            } else {
                teachers.add(dataObject.getSkjs());
            }
        }
        System.out.println("待找老师列表:" + teachers);


        ArrayList<String> targetTeachers = new ArrayList<>();
        targetTeachers.add(user.getFirstChoice());
        targetTeachers.add(user.getSecondChoice());

        //检查每个名字是否存在

        String myTeacher = "";
        for (String targetTeacher : targetTeachers) {
            String finalName = checkNameExistence(teachers, targetTeacher);
            if (finalName != null) {
                System.out.println("找到名字：" + finalName);
                myTeacher = finalName;
                break;
            } else {
                System.out.println("未找到老师：" + targetTeacher);

            }
        }
        if (myTeacher.equals("")) {
            System.out.println(user.getUsername() + "首选备选均未找到！");
            return false;
        }


        //如果到这一步，说明找到了老师的名称
        for (TeacherChooseData dataObject : dataObjects) {

            String skjs = dataObject.getSkjs();

            // 使用正则表达式去除括号及括号内的内容，只保留姓名部分
            String cleanedSkjs = skjs.replaceAll("\\(.*\\)", "");

            System.out.println(cleanedSkjs);
            if (!cleanedSkjs.equals(myTeacher)) {
                System.out.println("找到老师，但是不匹配？请检查程序的正则表达式");
                return false;
            }

            Double bkskrlDouble = dataObject.getBkskrl();
            // 将 Double 转换为 int
            int bkskrlInt = bkskrlDouble.intValue();
            // 将 int 转换为 String
            String bkskrl = String.valueOf(bkskrlInt);

            //请求体构造
            List<BasicNameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("xsxkList[0][Kch]", dataObject.getKch()));
            formParams.add(new BasicNameValuePair("xsxkList[0][Kcm]", dataObject.getKcm()));
            formParams.add(new BasicNameValuePair("xsxkList[0][Kxh]", dataObject.getKxh()));
            formParams.add(new BasicNameValuePair("xsxkList[0][Pid]", pid));
            formParams.add(new BasicNameValuePair("xsxkList[0][Bkskrl]", bkskrl));
            formParams.add(new BasicNameValuePair("xsxkList[0][Xf]", dataObject.getXf()));
            formParams.add(new BasicNameValuePair("logModel[Rolename]", ""));
            formParams.add(new BasicNameValuePair("logModel[Menuname]", "学生选课"));
            formParams.add(new BasicNameValuePair("isjwc", "false"));
            formParams.add(new BasicNameValuePair("encryptedUsername", RsaUtil.encryptID(user.getUsername())));
            formParams.add(new BasicNameValuePair("__RequestVerificationToken", user.getPageToken()));


            String content = HttpClientUtil.doPost("Tschedule/C4Xkgl/XsxkSaveForm", NODE, user.getCookieStore(), formParams);
            // 检查响应中是否包含"操作成功"
            Boolean b = checkResponseMessage(content);
            if (b) {
                return true;
            } else {
                System.out.println("抢课失败，原因：" + content);
            }

        }


        return true;
    }

    public Boolean checkResponseMessage(String response) {
        // 使用正则表达式检查响应中是否包含"操作成功"
        Pattern pattern = Pattern.compile("\"message\":\"操作成功\"");
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            System.out.println("操作成功");
            return true;
        } else {
            System.out.println("操作失败");
            return false;
        }
    }

    public void printTeachers(List<TeacherChooseData> dataObjects) {

        int j = 1;
        for (TeacherChooseData dataObject : dataObjects) {
            if (dataObject.getSfybm().equals("1")) {
                dataObject.setSfybm("【已选】");
            } else {
                dataObject.setSfybm("(未选)");
            }
            if (dataObject.getYbrs().intValue() >= dataObject.getBkskrl().intValue()) {

            } else {
                System.out.printf("%-10s%-20s%-20s%-20s%-20s%-20s%-20s%n",
                        j,
                        dataObject.getSkjs(),
                        dataObject.getKch(),
                        dataObject.getKcm(),
                        dataObject.getYbrs().intValue() + "/" + dataObject.getBkskrl().intValue(),
                        dataObject.getSkdd(),
                        dataObject.getSfybm());
            }

        }

    }

    public String checkNameExistence(ArrayList<String> teachers, String name) {
        // 构建一个正则表达式模式，这里简单地匹配整个名字

        for (String teacher : teachers) {
            Pattern pattern = Pattern.compile(name);
            Matcher matcher = pattern.matcher(teacher);
            if (matcher.find()) {
                System.out.println("找到了：" + name);
                return name;
            } else {
                return null;
            }
        }


        return null;
    }

    public void quitLesson(User user, List<TeacherChooseData> dataObjects, String pid) throws Exception {

        printTeachers(dataObjects);

        System.out.print("请输入要退出的课程号id：");
        Scanner sc = new Scanner(System.in);
        TeacherChooseData quitData = new TeacherChooseData();
        for (TeacherChooseData dataObject : dataObjects) {
            if (dataObject.getKch().equals(sc.next())) {
                quitData.setBkskrl(dataObject.getBkskrl());
                quitData.setBkskrl(dataObject.getBkskrl());
                quitData.setKch(dataObject.getKch());
                quitData.setKcm(dataObject.getKcm());
                quitData.setKxh(dataObject.getKxh());
                quitData.setXf(dataObject.getXf());
                break;
            }
        }

        //请求体构造
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("xsxkList[0][Kch]", quitData.getKch()));
        formParams.add(new BasicNameValuePair("xsxkList[0][Kcm]", quitData.getKcm()));
        formParams.add(new BasicNameValuePair("xsxkList[0][Kxh]", quitData.getKxh()));
        formParams.add(new BasicNameValuePair("xsxkList[0][Pid]", pid));
        formParams.add(new BasicNameValuePair("xsxkList[0][Bkskrl]", quitData.getBkskrl().toString()));
        formParams.add(new BasicNameValuePair("xsxkList[0][Xf]", quitData.getXf()));
        formParams.add(new BasicNameValuePair("xsxkList[0][Xh]", user.getUsername()));
        formParams.add(new BasicNameValuePair("isjwc", "false"));
        formParams.add(new BasicNameValuePair("encryptedUsername", RsaUtil.encryptID(user.getUsername())));
        formParams.add(new BasicNameValuePair("__RequestVerificationToken", user.getPageToken()));


        String content = HttpClientUtil.doPost("Tschedule/C4Xkgl/XsxkRemoveForm", NODE, user.getCookieStore(), formParams);

        //退课正则验证
        Boolean b = checkResponseMessage(content);
        if (b) {
            System.out.println("退课成功！");
        } else {
            System.out.println("失败，原因未知，请手动退课");
        }


    }
}
