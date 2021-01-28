package com.cdibd.elasticsearch.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cdibd.elasticsearch.bean.Product;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ESUtils {

    @Resource
    RestHighLevelClient restHighLevelClient;

    private static final Integer REST_STATUS_200 = 200;

    /**
     * 判断索引是否存在
     *
     * @param indexName
     * @return
     */
    public boolean isIndexExists(String indexName) {
        boolean exists = false;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            getIndexRequest.humanReadable(true);
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    public boolean deleteIndex(String indexName) {
        boolean acknowledged = false;
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            acknowledged = delete.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return acknowledged;
    }

    //创建索引方法，传入索引名和类型名
    public boolean createIndex(String index, String type) throws IOException {

        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        //设置分片和副本数
        request.settings(Settings.builder()
                .put("index.number_of_shards", 5)
                .put("index.number_of_replicas", 1)
                //.put("", null)
        );
        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");
        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);

        //创建索引
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        return true;

    }


    public boolean createBatch(String indexName, String type, List<Product> productList) {
        if (productList == null || productList.size() == 0) {
            return true;
        }
        //将id取出放入list
        List<String> ids = new ArrayList<>(productList.size());
        //创建map存放id和对应数据
        Map<String, String> idJsonParamMap = new HashMap<>(productList.size());
        for (Product product : productList) {
            String idStr = product.getId().toString();
            ids.add(idStr);
            String jsonParam = JSONObject.toJSONString(product,
                    SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            idJsonParamMap.put(idStr, jsonParam);
        }
        return createBatch(indexName, type, ids, idJsonParamMap);
    }

    public boolean createBatch(String index, String type, List<String> ids, Map<String, String> idJsonParamMap) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }
        //创建BulkRequest
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1m");
        //便利id的list,插入到BulkRequest中。
        for (String id : ids) {
            IndexRequest indexRequest = new IndexRequest(type).id(id).source(idJsonParamMap.get(id), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        try {
            //执行插入请求操作
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.status().getStatus() == REST_STATUS_200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void configAnalyzer(String indexName) {
        AnalyzeRequest.buildCustomNormalizer(indexName).;

    }


}