package com.sunrisejay.pojo;

import org.apache.http.Header;

import java.io.Serializable;


public class HttpClientResult implements Serializable {

    /**
     * 响应状态码
     */
    private int code;
    private Header[] cookies;

    /**
     * 响应数据
     */
    private String content;

    public HttpClientResult() {
    }

    public HttpClientResult(int code) {
        this.code = code;
    }

    public HttpClientResult(String content) {
        this.content = content;
    }

    public HttpClientResult(int code, String content, Header[] cookies) {
        this.code = code;
        this.content = content;
        this.cookies = cookies;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Header[] getCookies() {
        return cookies;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HttpClientResult [code=" + code + ", content=" + content + "]";
    }

}
