package com.Dolmeng_E.search.domain.search.repository;

import com.Dolmeng_E.search.domain.search.entity.TaskDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UnifiedSearchRepository extends ElasticsearchRepository<TaskDocument, String> {
    List<TaskDocument> findBySearchTitle(String title);
}
