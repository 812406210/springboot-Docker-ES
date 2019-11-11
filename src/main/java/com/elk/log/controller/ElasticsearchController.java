package com.elk.log.controller;

import com.elk.log.Dao.ItemRepository;
import com.elk.log.Dao.MySearchQuery;
import com.elk.log.model.Item;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "api/v1/es")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MySearchQuery queryBuilder;

    /**
     * 新增数据
     */
    @RequestMapping(value = "/insert")
    public void insert() {
        for (int i =15 ;i<20;i++) {
            Item item = new Item(Long.parseLong(i+""), "小米手机"+i, " 手机",
                    "小米", Double.valueOf(i+""), "http://image.baidu.com/13123.jpg");
            itemRepository.save(item);
        }

    }

    /**
     * 查询所有，降序排列
     */
    @RequestMapping(value = "/queryAll")
    public void queryAll() {
         Iterable<Item> itemIterable  =  itemRepository.findAll(Sort.by("price").descending());//降排序
        for (Item it :itemIterable) {
            System.out.println(it);
        }
    }

    /**
     * 价格区间查询,包含
     */
    @RequestMapping(value = "/queryByPrice")
    public void queryByPriceBetween(){
        List<Item> list = this.itemRepository.findByPriceBetween(Double.valueOf(5+""), Double.parseDouble(10+""));
        for (Item item : list) {
            System.out.println("item = " + item);
        }
    }


    /**
     * 自定义查询条件---->matchQuery(分词匹配)
     */
    @RequestMapping(value = "/matchQuery")
    public void matchQuery() {
        queryBuilder.withQuery(QueryBuilders.matchQuery("brand", "小米"));
        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Item item : items) {
            System.out.println(item);
        }
    }



    /**
     * 自定义查询条件---->termQuery(精确查找)
     */
    @RequestMapping(value = "/termQuery")
    public void termQuery() {
        queryBuilder.withQuery(QueryBuilders.termQuery("title", "手机"));
        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 自定义查询条件---->boolQuery(布尔查询)
     */
    @RequestMapping(value = "/boolQuery")
    public void boolQuery() {
        queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("brand", "小米"))
                                                        .must(QueryBuilders.matchQuery("id",6)));
        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 自定义查询条件---->fuzzyQuery(模糊查询)
     */
    @RequestMapping(value = "/fuzzyQuery")
    public void fuzzyQuery() {
        queryBuilder.withQuery(QueryBuilders.fuzzyQuery("title", "小米"));
        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/searchByPage")
    public void searchByPage(){
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("title", "小米"));
        // 分页：
        int page = 0;
        int size = 10;
        queryBuilder.withPageable(PageRequest.of(page,size));

        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("总条数 = " + total);
        // 总页数
        System.out.println("总页数 = " + items.getTotalPages());
        // 当前页
        System.out.println("当前页：" + items.getNumber());
        // 每页大小
        System.out.println("每页大小：" + items.getSize());

        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 排序
     */
    @RequestMapping(value = "/searchAndSort")
    public void searchAndSort(){
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("category", "手机"));

        // 排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));

        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("总条数 = " + total);

        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 聚合
     */
    @RequestMapping(value = "/testAgg")
    public void testAgg(){
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand"));
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        // 3、解析
        // 3.1、从结果中取出名为brands的那个聚合，
        // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        // 3.2、获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        // 3.3、遍历
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key，即品牌名称
            System.out.println(bucket.getKeyAsString());
            // 3.5、获取桶中的文档数量
            System.out.println(bucket.getDocCount());
        }

    }

    /**
     * 求平均值
     */
    @RequestMapping(value = "/testSubAgg")
    public void testSubAgg(){
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand")
                        .subAggregation(AggregationBuilders.avg("priceAvg").field("price")) // 在品牌聚合桶内进行嵌套聚合，求平均值
        );
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        // 3、解析
        // 3.1、从结果中取出名为brands的那个聚合，
        // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        // 3.2、获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        // 3.3、遍历
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key，即品牌名称  3.5、获取桶中的文档数量
            System.out.println(bucket.getKeyAsString() + "，共" + bucket.getDocCount() + "台");

            // 3.6.获取子聚合结果：
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
            System.out.println("平均售价：" + avg.getValue());
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
