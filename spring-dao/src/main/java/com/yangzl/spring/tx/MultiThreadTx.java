package com.yangzl.spring.tx;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.yangzl.spring.entity.Demo;
import com.yangzl.spring.mapper.DemoMapper;
import lombok.extern.java.Log;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yangzl
 * @date 2021/2/7
 * @desc
 *
 *  多线程事务管理：实现一个简易的2pc
 *
 *  TODO 封装一个方法，让调用者传入一个函数作为参数
 */

@Log
public class MultiThreadTx {

    @Resource
    private DemoMapper demoMapper;
    @Resource
    private TransactionTemplate transactionTemplate;


    /**
     * 多线程带事务提交数据
     *
     * @param threadNum 线程数
     */
    private void multiThreadTxTm(int threadNum) {

        /*
            1. 子线程挡在main上
            2. 主线程等待所有子线程执行完毕
            3. commitFlag标识多线程事务是否可以提交，当且仅当错误线程数>=1时，标记为不可提交
            4. 根据commitFlag提交 / 回滚多线程事务

            tips：到这里其实就实现了一个简易的2pc
         */
        CountDownLatch main = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(threadNum);
        AtomicBoolean commitFlag = new AtomicBoolean(true);

        long t1 = System.currentTimeMillis();
        /*
            spring事务管理器统一抽象
                1. DataSourceTransactionManager
                2. JdbcTransactionManager
                3. JtaTransactionManager
         */
        PlatformTransactionManager tm = transactionTemplate.getTransactionManager();
        for (int i = 0; i < threadNum; ++i) {
            CompletableFuture.runAsync(() -> {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        List<Demo> list = creataNumsData(20000);
                        sliceAndSave(list);
                    } catch (Exception e) {
                        commitFlag.set(false);
                    } finally {
                        log.info("============ a thread come in =======================");
                        latch.countDown();
                        try {
                            main.await();
                            // log.info("a thread execute rollback or commit");
                            if (commitFlag.get()) {
                                log.info("======== commit ============");
                                tm.commit(status);
                            } else {
                                log.info("======== rollback ==========");
                                tm.rollback(status);
                            }
                            log.info("100w take" + (System.currentTimeMillis() - t1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            });
        }

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("========= main countdown ===================");
            main.countDown();
        }
    }



    /**
     * 切分List<T>为List<List<T>>，按Const.SEGMENT分段
     *
     * @param datas datas
     * @param batchSize 每段大小
     * @param <T> 参数化类型
     * @return List<List>
     */
    public static <T> List<List<T>> sliceData(List<T> datas, int batchSize) {
        if (CollectionUtils.isEmpty(datas)) {
            return Collections.emptyList();
        }
        int sz = datas.size();
        List<List<T>> rs;
        if (sz < batchSize) {
            rs = new ArrayList<>(1);
            rs.add(datas);
            return rs;
        }
        int batch = sz / batchSize;
        rs = new ArrayList<>(batch);
        for (int i = 0; i < batch; ++i) {
            List<T> tmp;
            int begin = i * batchSize;
            if (i == batch - 1) {
                tmp = datas.subList(begin, sz);
            } else {
                tmp = datas.subList(begin, begin + batchSize);
            }
            rs.add(tmp);
        }
        return rs;
    }

    // =======================================================================================
    // divide
    // =======================================================================================


    /**
     * 切分并且保存到库
     *
     * @param list list
     */
    private void sliceAndSave(List<Demo> list) {
        List<List<Demo>> current = sliceData(list, 2000);
        for (List<Demo> tmp : current) {
            demoMapper.insertBatch(tmp);
        }
    }

    /**
     * 构建100w数据
     *
     * @return list
     */
    private List<Demo> creataNumsData(int size) {
        List<Demo> list = new ArrayList<>(size);
        final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < size; ++i) {
            Demo demo = new Demo();
            demo.setId(snowflake.nextId());
            demo.setName("name");
            demo.setAge(i & 31);
            demo.setSex("1");
            demo.setGmtCreate(new Date());
            demo.setStatus(1);

            list.add(demo);
        }
        return list;
    }
}
