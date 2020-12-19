package com.yangzl.spring.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/11/7 20:44
 * @desc
 */

@Slf4j
@Component
public class ScheduleTest {
    
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    
    /*
     * 由此可见，我们的配置生效了
     */
    @PostConstruct
    public void postConstruct() {
        System.out.printf("async thread pool ====> maxPoolSize: %s, poolSize: %s, corePoolSize: %s\n",
                threadPoolTaskExecutor.getMaxPoolSize(),
                threadPoolTaskExecutor.getPoolSize(),
                threadPoolTaskExecutor.getCorePoolSize());
    }
    
    /**
     * 2020/11/7 Spring cron表达式与 Quartz cron表达式的区别
     *      1. Spring 只允许 秒分时日月周、Quartz允许 秒分时日月周年
     *      2. Spring 调度任务线程池数量为1，请查看TaskSchedulingAutoConfiguration
     *      3. Spring 异步任务线程池默认8，最大为OX7fffffff，请查看TaskExecutionAutoConfiguration，并在配置文件中配置合理的线程池与队列大小
     *      4. cron 表达式，日和周的表示会冲突，所以其中一个请用 ?（代表无特定值） 表达式占位
     *      5. cron 表达式，L代表last，可用在日月周，L3代表最后3、 #代表第几 #3 代表第3
     *  
	 * @param () 无参
     * @return void 
     */
    @Scheduled(cron = "*/2 * * * * ?")
    public void testSchedule() {
        log.info("scheduling...");
        System.out.printf("schedule thread pool ====> poolSize: %s\n", threadPoolTaskScheduler.getPoolSize());
        // 这个poolSize会从1 增加到 2，此后一直保持2
        try { TimeUnit.SECONDS.sleep(10); } catch(InterruptedException e) { e.printStackTrace(); }
    }
}
