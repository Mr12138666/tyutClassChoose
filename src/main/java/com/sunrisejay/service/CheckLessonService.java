package com.sunrisejay.service;

import com.sunrisejay.constant.Constant;
import com.sunrisejay.pojo.User;
import com.sunrisejay.util.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

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

    private void showLesson(String content) {

        //todo 格式化打印
        System.out.println(content);

    }


}
