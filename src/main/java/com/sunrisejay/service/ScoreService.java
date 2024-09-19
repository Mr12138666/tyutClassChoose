package com.sunrisejay.service;

import com.sunrisejay.pojo.User;
import com.sunrisejay.util.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import static com.sunrisejay.constant.Constant.NODE;

/**
 * @auther Sunrise_Jay
 */

public class ScoreService {

    public void getScore(User user) throws Exception {
        //请求体构造
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("order", "zxjxjhh desc,kch"));


        String content = HttpClientUtils.doPost("Tschedule/C6Cjgl/GetKccjResult", NODE, user.getCookieStore(), formParams);

        //分数格式化打印
        Document doc = Jsoup.parse(content);
        org.jsoup.select.Elements tables = doc.select("table");

        for (Element table : tables) {
            org.jsoup.select.Elements rows = table.select("tr");

            for (Element row : rows) {
                org.jsoup.select.Elements cols = row.select("th,td");

                for (Element col : cols) {
                    System.out.print(col.text() + "\t");
                }
                System.out.println();
            }
            System.out.println("--------------------");
        }

    }


}
