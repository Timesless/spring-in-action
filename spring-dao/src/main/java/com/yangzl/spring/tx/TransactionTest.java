package com.yangzl.spring.tx;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yangzl
 * @date 2020/11/7 21:21
 * @desc 测试方法内事务配置失效
 * 
 *  JDK动态代理只能代理接口，方法内调用其它方法，是没有走代理对象的。所以事务配置失效围绕代理对象即可解决
 *  更换为 CGLIB 动态代理可解决事务配置失效问题，GGLIB通过ASM字节码技术
 */

@Component
@Transactional(rollbackFor = Exception.class)
public class TransactionTest {
    
    
}
