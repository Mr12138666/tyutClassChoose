package com.sunrisejay.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Course implements Serializable {


    private String Kcm; // 课程名称 数学

    private String Jsm; // 教师名称  xxx

    private String Zcsm; // 周次说明  1-14周

    private Integer Skxq; // 上课星期  2

    private String Jc; // 节次    3~4

    private String Dd; // 上课地点 明向 行知楼 C411


    @ExcelIgnore
    private String Zxjxjhh; // 学期号
    @ExcelIgnore
    private String Kch; // 课程号
    @ExcelIgnore
    private String Kxh; // 课程序号
    @ExcelIgnore
    private double Xf; // 学分
    @ExcelIgnore
    private String Kclb; // 课程类别
    @ExcelIgnore
    private String Kcsx; // 课程属性
    @ExcelIgnore
    private String Kslx; // 考试类型
    @ExcelIgnore
    private String Skzc; // 上课周次
    @ExcelIgnore
    private String Xqm; // 校区名称
    @ExcelIgnore
    private String Jxlm; // 教学楼名称
    @ExcelIgnore
    private String Jasm; // 教室名称
    @ExcelIgnore
    private Double Skjc; // 上课节次
    @ExcelIgnore
    private Double Cxjc; // 持续节次
}
