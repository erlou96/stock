package com.binzaijun.stock.util;

import com.alibaba.fastjson2.JSONObject;
import com.binzaijun.stock.domain.SinaStock;
import com.binzaijun.stock.domain.StockInfo;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EsUtil {

    private static final String HOST = "59.110.32.152";
    private static final String STOCK = "stock";
    private static final String TYPE = "_doc";
    private static final String NAME = "elastic";
    private static final String PASSWORD = "han1996";

    /**
     * 新建 es 连接
     * @return
     */
    public static RestHighLevelClient createClient() {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(NAME, PASSWORD));

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST, 9200, "http"),
                        new HttpHost(HOST, 9201, "http"))
                        .setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider)));
        return client;
    }

    /**
     * bulk导入日k数据
     * @throws IOException
     */
    public static void bulkEsData(List<SinaStock> sinaStockList) throws IOException {

        RestHighLevelClient client = createClient();
        // 创建一个 BulkRequest 对象
        BulkRequest bulkRequest = new BulkRequest();

        // 构建多个 IndexRequest 添加到 BulkRequest 中
        for (SinaStock stock : sinaStockList) {
            bulkRequest.add(new IndexRequest(STOCK, TYPE)
                    .source(JSONObject.from(stock), XContentType.JSON));
        }


        // 执行 BulkRequest 操作
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            // 处理响应结果
            if (bulkResponse.hasFailures()) {
                // 处理失败情况
                System.out.println("Bulk request failed:");
                System.out.println(bulkResponse.buildFailureMessage());
            } else {
                // 成功情况
                System.out.println("Bulk request successful!");
            }
        }catch (Exception e) {
            System.out.println(e);
        }

        // 关闭客户端连接
        client.close();

    }


    public static SearchResponse selectData(String queryString) {
        RestHighLevelClient client = createClient();
        // 创建一个 BulkRequest 对象
        BulkRequest bulkRequest = new BulkRequest();

        // 定义查询字段和查询字符串
        String[] fields = {"stock_name.pinyin", "stock_name.hanlp", "stock_symbol"};

        // 构建 multi_match 查询
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(queryString, fields)
                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                .tieBreaker(0.1f);

        // 构建查询条件和排序
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(multiMatchQuery);
//        sourceBuilder.sort(SortBuilders.fieldSort("stock_name.hanlp").order(SortOrder.ASC));

        // 构建 SearchRequest
        SearchRequest searchRequest = new SearchRequest("stock_info");
        searchRequest.source(sourceBuilder);

        // 执行查询
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            // 处理查询结果
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static SearchResponse selectAllData() {
        RestHighLevelClient client = createClient();
        try {
            // 构建查询请求
            SearchRequest searchRequest = new SearchRequest("stock_info");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(sourceBuilder);

            // 执行查询
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 处理查询结果
            System.out.println("Total Hits: " + searchResponse.getHits().getTotalHits());
            // 处理其他结果信息...
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        selectData("zgcb");
        selectAllData();
    }
}
