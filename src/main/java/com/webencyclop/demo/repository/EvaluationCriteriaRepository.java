package com.webencyclop.demo.repository;



import com.webencyclop.demo.model.EvaluationCriteria;
import com.webencyclop.demo.model.RatingCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Integer>{

    @Query
    public EvaluationCriteria findByOneCriterion(String criteria);

}

