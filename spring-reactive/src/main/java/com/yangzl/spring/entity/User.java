package com.yangzl.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yangzl
 * @date 2020/12/19 11:32
 * @desc
 */

@Data
@AllArgsConstructor
public class User {
    
    private Integer id;
    private String name;
    private Integer age;
    private Integer sex;
    public User() {}
}
