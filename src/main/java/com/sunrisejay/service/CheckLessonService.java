package com.sunrisejay.service;

import com.sunrisejay.pojo.User;

import java.io.IOException;

/**
 * @auther Sunrise_Jay
 */
public interface CheckLessonService {

    void getLesson(User user) throws Exception;

    void showLesson(String content) throws IOException;



}
