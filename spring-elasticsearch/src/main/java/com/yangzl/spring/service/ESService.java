package com.yangzl.spring.service;

import java.util.List;
import java.util.Map;

/**
 * @author yangzl
 * @date 2020/10/3 00:10
 * @desc
 */
public interface ESService {
    
    /**
     * 2020/10/3 关键字搜索并解析京东网页，存入本地ES
     * 
     * @param keyword 搜索关键字
     * @return boolean
     */
    boolean addDoc(String keyword);
    
    /**
     * 2020/10/3 搜索 
     * 
     * @param keyword 查询关键词
     * @return List
     */
    List<Map<String, Object>> searchDoc(String keyword);

    /**
     * 2020/10/3 模糊搜索
     * 
     * @param keyword 关键字
     * @param curPage 当前页
     * @param pageSize 分页大小
     * @return List
     */
    List<Map<String, Object>> searchDoc(String keyword, int curPage, int pageSize);
    
    /**
     * 2020/10/3 term 精确匹配
     * 
     * @param keyword 查询关键词
     * @return  List
     */
    List<Map<String, Object>> searchDocByTerm(String keyword);

    /**
     * 2020/12/18 分页查询
     * 
	 * @param keyword 查询关键词
	 * @param curPage 当前页
	 * @param pageSize 分页大小
     * @return List
     */
    List<Map<String, Object>> searchDocByTerm(String keyword, int curPage, int pageSize);
}
