package com.sunrisejay.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // 自动生成 getter 和 setter
@NoArgsConstructor // 自动生成无参构造器
@AllArgsConstructor // 自动生成全参数构造器
public class ClassChooseData {

    private String Id;
    private String Describe;
    private String Ssxq;
    private String Pid;
    private String Creater;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date Addtime;
    private String Modifier;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date Modifytime;
    private String Valid;
    private String Zxjxjhh;
    private String Begintime;
    private String Endtime;
    private String Xsh;
}