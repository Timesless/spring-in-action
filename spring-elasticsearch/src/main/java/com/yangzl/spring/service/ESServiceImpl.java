package com.yangzl.spring.service;

import com.alibaba.fastjson.JSON;
import com.yangzl.spring.domain.JDBook;
import com.yangzl.spring.util.HtmlParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2020/10/3 00:10
 * @desc
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ESServiceImpl implements ESService {

    private static final String INDEX_NAME = "jd";
    
    private RestClient lowerClient;
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }
    
    @PostConstruct
    public void intialLowerClient() {
        lowerClient = restHighLevelClient.getLowLevelClient();
    }

    // ============================================================

    
    @Override
    public boolean addDoc(String keyword) {
        try {
            if (!indexExist()) {
                CreateIndexRequest createRequest = new CreateIndexRequest(INDEX_NAME);
                CreateIndexResponse res =restHighLevelClient.indices()
                        .create(createRequest, RequestOptions.DEFAULT);
                boolean ack = res.isAcknowledged();
                if (!ack) {
                    return false;
                }
            }
            // 将解析的文档保存至ES
            List<JDBook> contents = HtmlParseUtil.parseHtml(keyword);
            BulkRequest bulkRequest = new BulkRequest();
            contents.forEach(content -> bulkRequest.add(new IndexRequest(INDEX_NAME)
                    .id(content.getId())
                    .source(JSON.toJSONString(content), XContentType.JSON)));
            bulkRequest.timeout(TimeValue.timeValueSeconds(4));
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return !bulkResponse.hasFailures();
        } catch (IOException e) {
            log.error("连接ES异常", e);
        }
       return false;
    }

    @Override
    public List<Map<String, Object>> searchDoc(String keyword) {
        return searchDoc(keyword, 0, 20);
    }
    
    @Override
    public List<Map<String, Object>> searchDoc(String keyword, int curPage, int pageSize) {
        QueryBuilder matchBuilder = QueryBuilders.matchQuery("name", keyword);
        return query(matchBuilder, curPage, pageSize);
    }

    @Override
    public List<Map<String, Object>> searchDocByTerm(String keyword) {
        return searchDocByTerm(keyword, 0, 20);
    }

    @Override
    public List<Map<String, Object>> searchDocByTerm(String keyword, int curPage, int pageSize) {
        
        QueryBuilder termBuilder = QueryBuilders.termQuery("name", keyword);
        return query(termBuilder, curPage, pageSize);
    }

    // =======================================================
    
    private boolean indexExist() throws IOException {
        GetIndexRequest getRequest = new GetIndexRequest(INDEX_NAME);
        return restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
    }
    
    
    private List<Map<String, Object>> query(QueryBuilder queryBuilder, int curPage, int pageSize) {
        List<Map<String, Object>> rs = Collections.emptyList();
        try {
            if (!indexExist()) {
                return rs;
            }
            SearchRequest searchRequest = new SearchRequest();
            SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
            // bool / term / match
            searchBuilder.query(queryBuilder);
            // 查询设置
            searchBuilder.timeout(TimeValue.timeValueSeconds(2));
            searchBuilder.from(curPage);
            searchBuilder.size(pageSize);
            searchRequest.source(searchBuilder);
            SearchResponse res = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = res.getHits().getHits();
            return Stream.of(hits).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("连接ES异常", e);
        }
        return rs;
    }
}
