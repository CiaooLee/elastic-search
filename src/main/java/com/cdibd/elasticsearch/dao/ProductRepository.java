package com.cdibd.elasticsearch.dao;

import com.cdibd.elasticsearch.bean.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product,String> {

    List<Product> findProductByName(String name);
}
