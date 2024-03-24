package com.seciii.prism030.core.dao.es;

import com.seciii.prism030.core.pojo.po.es.NewsESPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsESDao extends ElasticsearchRepository<NewsESPO, Long> {

    void deleteById(Long id);

    NewsESPO findByNewsId(Long id);


    List<NewsESPO> findByTitle(String title);
}
