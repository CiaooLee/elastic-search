package com.cdibd.elasticsearch.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Document(indexName = "product")
public class Product implements Serializable {

        @Id
        @Field(type= FieldType.Keyword)
        private String id;

        /**
         * 产品名称
         */
        private String name;

        /**
         * 产品名称(英文)
         */
        private String nameEn;

}
