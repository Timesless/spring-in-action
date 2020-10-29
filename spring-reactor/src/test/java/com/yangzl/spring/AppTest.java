package com.yangzl.spring;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Author yangzl
 * @Date: 2020/9/1 09:50
 * @Desc: Junit5使用@ExtendWith注解，代替Junit4@Runwith
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReactorApplication.class)
public class AppTest {

}
