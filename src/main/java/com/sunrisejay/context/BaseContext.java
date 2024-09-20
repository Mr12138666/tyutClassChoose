package com.sunrisejay.context;

import com.sunrisejay.pojo.User;

public class BaseContext {

    public static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static User getCurrentId() {
        return threadLocal.get();
    }

    public static void setCurrentId(User user) {
        threadLocal.set(user);
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
