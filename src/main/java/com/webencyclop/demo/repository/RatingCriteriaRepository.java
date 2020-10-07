package com.webencyclop.demo.repository;

import com.webencyclop.demo.model.RatingCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingCriteriaRepository extends JpaRepository<RatingCriteria, Integer> {

    @Query
    public RatingCriteria findByOneCriterion(String criteria);

}

