package com.yangzl.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangzl
 * @date 2020/10/3 17:44
 * @desc
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JDBook {
    
    private transient String id;
    private String name;
    private String price;
    private String img;
}
