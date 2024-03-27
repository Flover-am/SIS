package com.seciii.prism030.core.dao.es;

import com.seciii.prism030.core.pojo.po.es.ESNewsPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESNewsDao extends ElasticsearchRepository<ESNewsPO, Long> {

    int deleteByNewsId(Long id);

    ESNewsPO findByNewsId(Long id);

    List<ESNewsPO> findByTitle(String title);
}
