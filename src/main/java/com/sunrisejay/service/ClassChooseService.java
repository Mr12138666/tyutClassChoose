package com.sunrisejay.service;

import com.sunrisejay.pojo.TeacherChooseData;
import com.sunrisejay.pojo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Sunrise_Jay
 */
public interface ClassChooseService {

    String getCourseList(User user) throws Exception;

    String getFirstCourseUID(User user, String courseList);

    String getCourseDetail(User user, String uid) throws Exception;

    Boolean tryCatchTeacher(User user, String courseDetail, String pid, List<TeacherChooseData> dataObjects) throws Exception;

    Boolean checkResponseMessage(String response);

    void printTeachers(List<TeacherChooseData> dataObjects);

    String checkNameExistence(ArrayList<String> teachers, String name);

    void quitLesson(User user, List<TeacherChooseData> dataObjects, String pid) throws Exception;
}
