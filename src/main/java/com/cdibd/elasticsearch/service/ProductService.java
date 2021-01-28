package com.cdibd.elasticsearch.service;

import com.cdibd.elasticsearch.bean.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> findProductByName(String name);
    boolean createIndex(String indexName,String type) throws IOException;
    Product save(Product product);
    Iterable<Product> saveAll(Iterable<Product> productList);
    void deleteById(String id);
    void deleteAll();

    List<Product> getProducts();

    boolean createBatch(List<Product> productList);
}
