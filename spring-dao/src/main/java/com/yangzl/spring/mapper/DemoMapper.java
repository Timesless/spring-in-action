package com.yangzl.spring.mapper;

import com.yangzl.spring.entity.Demo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangzl
 * @date 2021/2/7
 * @desc
 */

public interface DemoMapper {

    /**
     * 批量insert
     *
     * @param list list
     */
    void insertBatch(@Param("list")List<Demo> list);

}
