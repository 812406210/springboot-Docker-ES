package com.elk.log.Dao;

import com.elk.log.model.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

    List<Item> findByPriceBetween(Double price1, Double price2);

}

