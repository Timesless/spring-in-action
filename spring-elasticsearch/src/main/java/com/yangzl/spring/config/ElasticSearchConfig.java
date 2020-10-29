package com.yangzl.spring.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author yangzl
 * @date 2020/10/3 00:04
 * @desc
 */

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        /*
         * .useSsl()                                                             
            .withProxy("localhost:8888")                                          
            .withPathPrefix("ela")                                                
            .withConnectTimeout(Duration.ofSeconds(5))                            
            .withSocketTimeout(Duration.ofSeconds(3))                             
            .withDefaultHeaders(defaultHeaders)                                   
            .withBasicAuth(username, password)                                    
            .withHeaders(() -> {                                                  
                HttpHeaders headers = new HttpHeaders();
                headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                return headers;
            })
         */
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
