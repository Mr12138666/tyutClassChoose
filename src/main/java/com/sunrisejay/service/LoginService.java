package com.sunrisejay.service;

import com.sunrisejay.pojo.User;

/**
 * @auther Sunrise_Jay
 */
public interface LoginService {
    String extractRequestVerificationToken(String indexInfo);

    User login(User user) throws Exception;

}
