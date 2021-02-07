package com.yangzl.spring.autoconfigure.threadpool;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangzl
 * @date 2021/1/21
 * @desc 读取配置文件
 */

@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {

    private Integer cpuItensive;

    public Integer getCpuItensive() {
        return cpuItensive;
    }

    public void setCpuItensive(Integer cpuItensive) {
        this.cpuItensive = cpuItensive;
    }
}
