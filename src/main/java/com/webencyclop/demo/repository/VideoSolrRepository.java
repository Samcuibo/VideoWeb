package com.webencyclop.demo.repository;

import com.webencyclop.demo.model.VideoSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface VideoSolrRepository extends SolrCrudRepository<VideoSolr,String > {
}

