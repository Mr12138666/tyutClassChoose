package com.sunrisejay.service;

import com.sunrisejay.pojo.User;
import com.sunrisejay.util.HttpClientUtils;
import com.sunrisejay.util.RsaUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sunrisejay.constant.Constant.NODE;

/**
 * @auther Sunrise_Jay
 */

public class LoginService {
    CookieStore cookieStore = new BasicCookieStore();

    private static String extractRequestVerificationToken(String indexInfo) {
        // 定义正则表达式
        Pattern pattern = Pattern.compile("<input.*?name=\"__RequestVerificationToken\".*?value=\"(.*?)\".*?>");

        // 创建 Matcher 对象
        Matcher matcher = pattern.matcher(indexInfo);

        if (matcher.find()) {
            return matcher.group(1); // 返回匹配的第一个子组
        }
        return null; // 如果没有找到匹配项，返回 null
    }

    public User login(User user) throws Exception {


        //进行登录
        //请求体构造
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("username", RsaUtil.encryptID(user.getUsername())));
        formParams.add(new BasicNameValuePair("password", user.getPassword()));
        formParams.add(new BasicNameValuePair("code", null));
        formParams.add(new BasicNameValuePair("isautologin", "0"));


        String content = HttpClientUtils.doPost("Login/CheckLogin", NODE, cookieStore, formParams);


        // 正则表达式匹配 "type": 后面的数字
        Pattern pattern = Pattern.compile("\"type\":(\\d+)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String type = matcher.group(1); // 提取数字部分
            if (type.equals("1")) {
                System.out.println("登录成功");
                String getTokenFormContent = HttpClientUtils.doGet("Tschedule/C4Xkgl/XkXsxkIndex?yhm=R3m18OaKO5IALl6PvzDPz6qFtj8pRuCcSydIgHmMN2Ios2c5yfv/d2Aup8pgksJXWQZE/L8wbkZ9vrOSTFdX8jr5Jz4ewiSmYICXNSWViR/vPCgx8bHgcYY+VR4JKT5siATlDPehxIhI25pvgb36g0rba4/3wQQ4nKYUYpVR0+4=&mm=c237053a74zyc431a8a9a6a46748", NODE, cookieStore);
                String pageToken = extractRequestVerificationToken(getTokenFormContent);
                user.setPageToken(pageToken);
                user.setCookieStore(cookieStore);
                return user;
            } else if (type.equals("0")) {
                System.out.println("登录失败:" + user.getUsername() + "的原密码强度不够或者天数间隔过长，请修改密码！");
                return null;
            }
        } else {
            System.out.println("登录失败！请检查" + user.getUsername() + "的账号密码是否正确！");
            return null;
        }
        System.out.println("发生未知错误");
        return null;
    }

}
