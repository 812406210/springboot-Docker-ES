package com.elk.log.controller;

import com.elk.log.Dao.ItemRepository;
import com.elk.log.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/es")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    private ItemRepository itemRepository;

    /**
     * 新增
     */
    @RequestMapping(value = "/insert")
    public void insert() {
        for (int i =0 ;i<5;i++) {
            Item item = new Item(Long.parseLong(i+""), "华为手机", " 手机",
                    "华为", Double.valueOf(i+""), "http://image.baidu.com/13123.jpg");
            itemRepository.save(item);
        }

    }

    /**
     * 增加索引
     */
    @RequestMapping(value = "/createIndex")
    public void testCreateIndex() {
        elasticsearchTemplate.createIndex(Item.class);
    }

    /**
     * 删除索引
     */
    @RequestMapping(value = "/deleteIndex")
    public void testDeleteIndex() {
        elasticsearchTemplate.deleteIndex(Item.class);
    }



}
