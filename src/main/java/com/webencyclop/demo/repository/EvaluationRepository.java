package com.webencyclop.demo.repository;

import com.webencyclop.demo.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer>{

    @Query(value = "from Evaluation where videoAddress = ?1 and ratedBy = ?2 and criterion = ?3")
    public Evaluation findByVideoAddressUserAndCriterion (String Address,String User,String criterion);

    @Query(value = "from Evaluation where videoAddress = ?1 and criterion= ?2")
    public List<Evaluation> findByVideoAddressAndCriterion(String Address, String criterion);
}

