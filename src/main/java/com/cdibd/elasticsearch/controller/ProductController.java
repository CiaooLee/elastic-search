package com.cdibd.elasticsearch.controller;

import com.cdibd.elasticsearch.bean.Product;
import com.cdibd.elasticsearch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/query")
    public List<Product> queryProduct(String name){
        return productService.findProductByName(name);
    }

    @RequestMapping("/createIndex")
    public boolean createIndex(String indexName,String type) throws IOException {
        return productService.createIndex(indexName,type);
    }

    @RequestMapping("/insertData")
    public Product insertData(@RequestBody Product product){
        return productService.save(product);
    }

    @RequestMapping("/batchInsertData")
    public Iterable<Product> batchInsertData(@RequestBody List<Product> productList){
        return productService.saveAll(productList);
    }

    @RequestMapping("/bulkIndex")
    public boolean bulkIndex(@RequestBody List<Product> productList){
        return productService.createBatch(productList);
    }

    @RequestMapping("/deleteById")
    public void deleteById(String id){
        productService.deleteById(id);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll(){
        productService.deleteAll();
    }

    @RequestMapping("/importToES")
    public void importDataToES(){
        List<Product> productList = productService.getProducts();
        //productService.saveAll(productList);
    }
}
