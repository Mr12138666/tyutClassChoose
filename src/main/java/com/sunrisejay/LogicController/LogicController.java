package com.sunrisejay.LogicController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunrisejay.pojo.TeacherChooseData;
import com.sunrisejay.pojo.User;
import com.sunrisejay.service.impl.CheckLessonServiceImpl;
import com.sunrisejay.service.impl.ClassChooseServiceImpl;
import com.sunrisejay.service.impl.LoginServiceImpl;
import com.sunrisejay.service.impl.ScoreServiceImpl;

import java.util.List;
import java.util.Scanner;

/**
 * @auther Sunrise_Jay
 */

public class LogicController {


    public void start() throws Exception {
        User logined = login();
        functionChoose(logined);
    }

    private void functionChoose(User user) throws Exception {

        System.out.println("请选择你要使用的功能：");
        System.out.println("1.查询课表");
        System.out.println("2.选课");
        System.out.println("3.退课");
        System.out.println("4.成绩查询");
        System.out.println("您的选择是：");
        System.out.print("Sunrise_Jay > ");

        Scanner sc = new Scanner(System.in);
        String choice = sc.next();

        switch (choice) {

            case "1":
                checkLesson(user);
                break;
            case "2":
                classChoose(user, 2);
                break;
            case "3":
                classChoose(user, 3);
                break;
            case "4":
                score(user);
                break;
            default:
                System.out.println("输入有误，请重新输入！");
                break;
        }

    }

    private void score(User user) throws Exception {
        ScoreServiceImpl scoreService = new ScoreServiceImpl();
        scoreService.getScore(user);


    }

    private void classChoose(User user, int i) throws Exception {

        ClassChooseServiceImpl classChooseService = new ClassChooseServiceImpl();
        //执行到此处说明是选课
        String courseList = classChooseService.getCourseList(user);
        //返回列表首个课程的UID(因为学校一般是只放出一个科目的选课表，故默认处理首个)
        String firstCourseUID = classChooseService.getFirstCourseUID(user, courseList);
        if (firstCourseUID == null) {
            return;
        }
        //带着这个uid去获取该课程的所有待选老师列表
        String courseDetail = classChooseService.getCourseDetail(user, firstCourseUID);
        //将老师信息提取出来并封装
        JSONObject jsonObject = JSONObject.parseObject(courseDetail);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        List<TeacherChooseData> dataObjects = jsonArray.toJavaList(TeacherChooseData.class);
        //根据i=3判断退课
        if (i == 3) {
            classChooseService.quitLesson(user, dataObjects, firstCourseUID);

            return;
        }


        //用于打印老师列表(仅打印未满的)
        classChooseService.printTeachers(dataObjects);

        Scanner sc = new Scanner(System.in);
        System.out.println("你要选择的老师是：");
        System.out.print("首选：");
        user.setFirstChoice(sc.next());
        System.out.print("备选：");
        user.setSecondChoice(sc.next());


        //利用正则去匹配每一个学生的待选老师，如果待选老师已满/不存在，则匹配备选老师。
        //若都不存在，则打印错误提示，并进入下一个学生的循环。
        //若存在，则选择！
        //为了随时可视化待选老师名单，其内部包含了打印老师名单的方法
        Boolean flag = classChooseService.tryCatchTeacher(user, courseDetail, firstCourseUID, dataObjects);
        if (flag) {
            System.out.println("抢课成功");
        } else {
            System.out.println("抢课失败");
        }
    }

    private void checkLesson(User user) throws Exception {

        CheckLessonServiceImpl checkLessonServiceImpl = new CheckLessonServiceImpl();
        checkLessonServiceImpl.getLesson(user);


    }

    private User login() throws Exception {
        LoginServiceImpl loginService = new LoginServiceImpl();
        User user = new User();

        System.out.println("欢迎使用教务管理系统！");
        System.out.println("请登录");

        Scanner sc = new Scanner(System.in);

        System.out.print("请输入学号：");
        user.setUsername(sc.next());
        System.out.print("请输入密码：");
        user.setPassword(sc.next());
        User logined = loginService.login(user);
        if (logined == null) {
            System.out.println("登录失败！");
            return null;
        }


        return logined;
    }

}
