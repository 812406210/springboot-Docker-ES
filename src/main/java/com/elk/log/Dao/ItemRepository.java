package com.elk.log.Dao;

import com.elk.log.model.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

}

