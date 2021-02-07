package com.yangzl.spring.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author yangzl
 * @date 2021/2/7
 * @desc
 */

@Data
public class Demo {

    private Long id;
    private String name;
    private Integer age;
    private String sex;
    private Date gmtCreate;
    private Integer status;
}
