package com.seciii.prism030.core.dao.es;

import com.seciii.prism030.core.pojo.po.es.ESNewsPO;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESNewsDao extends ElasticsearchRepository<ESNewsPO, Long> {

    void deleteByNewsId(Long id);

    ESNewsPO findByNewsId(Long id);

    @Query("{\"match\": {\"title\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ESNewsPO> findByTitleFuzzy(String title);

}
