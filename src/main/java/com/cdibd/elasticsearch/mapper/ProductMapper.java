package com.cdibd.elasticsearch.mapper;

import com.cdibd.elasticsearch.bean.Product;

import java.util.List;

public interface ProductMapper{
    List<Product> selectList();
}
