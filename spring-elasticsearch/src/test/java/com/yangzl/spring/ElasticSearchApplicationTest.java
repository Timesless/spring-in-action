package com.yangzl.spring;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author yangzl
 * @date 2020/10/3 00:28
 * @desc
 * 
 * IndexOperations defines actions on index level like creating or deleting an index.
 *
 * DocumentOperations defines actions to store, update and retrieve entities based on their id.
 *
 * SearchOperations define the actions to search for multiple entities using queries
 *
 * ElasticsearchOperations combines the DocumentOperations and SearchOperations interfaces.
 */

@SpringBootTest
public class ElasticSearchApplicationTest {
    
    @Resource
    private RestHighLevelClient elasticsearchClient;
    
    
    /**
     * 2020/10/3 创建索引
     */
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest req = new CreateIndexRequest("myidx");
        CreateIndexResponse res =
                elasticsearchClient.indices().create(req, RequestOptions.DEFAULT);

        System.out.println(res);
    }
    
    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest req = new GetIndexRequest("myidx");
        boolean exists = elasticsearchClient.indices().exists(req, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest req = new DeleteIndexRequest("myidx");
        AcknowledgedResponse res =
                elasticsearchClient.indices().delete(req, RequestOptions.DEFAULT);
        System.out.println(res.isAcknowledged());
    }
    
    
    /*
     * 创建文档
     */
    @Test
    public void testCreateDoc() throws IOException {
        IndexRequest req = new IndexRequest("myidx");
        req.id("1001");
        Map<String, Object> jsonMap = new HashMap<>(8);
        jsonMap.put("name", "yangzl");
        jsonMap.put("age", 24);
        req.source(JSON.toJSONString(jsonMap), XContentType.JSON);
        IndexResponse res =
                elasticsearchClient.index(req, RequestOptions.DEFAULT);
        System.out.println(res.toString());
    }
    
    /*
     * 获取文档
     */
    @Test
    public void testGetDoc() throws IOException {
        GetRequest req = new GetRequest("myidx", "1001");
        // 不返回上下文
        req.fetchSourceContext(new FetchSourceContext(false));
        req.storedFields("_none_");

        boolean exists = elasticsearchClient.exists(req, RequestOptions.DEFAULT);
        if (!exists) {
            return;
        }
        GetResponse response = elasticsearchClient.get(req, RequestOptions.DEFAULT);
        System.out.println(response);
        System.out.println(response.getSourceAsString());
        
    }
    
    /*
     * 批量操作 创建
     */
    @Test
    public void testCreateDocBulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));
        IntStream.range(0, 10).forEach(i -> {
            Map<String, Object> map = new HashMap(4) {{
                put("name", "yangzl" + i);
                put("age", 24);
            }};
            bulkRequest.add(new IndexRequest("myidx")
                    .id(String.valueOf(i))
                    .source(JSON.toJSONString(map), XContentType.JSON));
        });
        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse);
    }
    
    /*
     * 复杂查询
     */
    @Test
    public void testComplexSearch() throws IOException {
        SearchRequest request = new SearchRequest("myidx");
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        TermQueryBuilder termBuilder = QueryBuilders.termQuery("name", "yangzl");
        searchBuilder.query(termBuilder);
        searchBuilder.timeout(TimeValue.timeValueSeconds(5));
        
        request.source(searchBuilder);
        SearchResponse response = elasticsearchClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        for (SearchHit cur : hits) {
            System.out.println(cur.getSourceAsString());
        }
    }
}
