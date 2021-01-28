package com.cdibd.elasticsearch.service.impl;

import com.cdibd.elasticsearch.bean.Product;
import com.cdibd.elasticsearch.dao.ProductRepository;
import com.cdibd.elasticsearch.mapper.ProductMapper;
import com.cdibd.elasticsearch.service.ProductService;
import com.cdibd.elasticsearch.util.ESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String INDEX_NAME = "product";

    private static final String TYPE = "product";

    @Resource
    private ESUtils esUtils;

    @Autowired
    private ProductRepository productRepository;

    @Resource
    private ProductMapper productMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public List<Product> findProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Override
    public boolean createIndex(String indexName,String type) throws IOException {
        indexName = indexName == null || indexName.length()==0?INDEX_NAME:indexName;
        type = type == null || type.length()==0?TYPE:type;
        if(esUtils.isIndexExists(indexName)){
            esUtils.deleteIndex(indexName);
        }
        return esUtils.createIndex(indexName, type);
    }


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Iterable<Product> saveAll(Iterable<Product> productList) {
        return productRepository.saveAll(productList);
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Override
    public List<Product> getProducts() {
        return productMapper.selectList();
    }

    @Override
    public boolean createBatch(List<Product> productList) {

        return esUtils.createBatch(INDEX_NAME, TYPE, productList);
        
    }
}
