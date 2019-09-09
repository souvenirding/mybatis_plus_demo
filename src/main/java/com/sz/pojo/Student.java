package com.sz.pojo;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tblstudent")
public class Student {
    @TableId
    private String stuid;
    private String stuname;
    private int stuage;
    private String stusex;
}
