package com.yangzl.spring.controller;

import com.yangzl.spring.service.ESService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yangzl
 * @date 2020/10/3 00:08
 * @desc
 */

@RestController
public class ESJDController {
    
    @Resource
    private ESService service;
    
    @GetMapping("parse/{keyword}")
    public boolean addDoc(@PathVariable String keyword) {
        return service.addDoc(keyword);
    }
    
    
    /**
     * 2020/10/3 分词搜索 
     * 
     * @param 
     * @return 
     */
    @GetMapping("search/{keyword}")
    public List<Map<String, Object>> searchDocByKeyword(@PathVariable String keyword) {
        return service.searchDoc(keyword);
    }
    
    @GetMapping("search/{keyword}/{curPage}/{pageSize}")
    public List<Map<String, Object>> searchDocWithPageByKeyword(@PathVariable String keyword,
                                          @PathVariable int curPage,
                                          @PathVariable int pageSize) {
        return service.searchDoc(keyword, curPage, pageSize);
    }
    
    /**
     * 2020/10/3 term 精确搜索 
     * 
     * @param 
     * @return 
     */
    @GetMapping("term/{keyword}")
    public List<Map<String, Object>> searchDocByTerm(@PathVariable String keyword) {
        return service.searchDocByTerm(keyword);
    }

    @GetMapping("term/{keyword}/{curPage}/{pageSize}")
    public List<Map<String, Object>> searchDocWithPageByTerm(@PathVariable String keyword,
                                                                @PathVariable int curPage,
                                                                @PathVariable int pageSize) {
        return service.searchDocByTerm(keyword, curPage, pageSize);
    }
    
}
