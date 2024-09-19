package com.sunrisejay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.CookieStore;

/**
 * @auther Sunrise_Jay
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;
    private String password;
    private String pageToken;
    private CookieStore cookieStore;
    private String firstChoice;
    private String SecondChoice;


    //  private String code;
    //  private final String isautologin = "0";


}
