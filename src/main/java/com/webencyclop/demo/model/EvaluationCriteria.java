package com.webencyclop.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "evaluation_criteria")
public class EvaluationCriteria {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_criterion_id")
    private int id;


    @Column(name = "one_evaluation_criterion")
    private String oneCriterion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOneCriterion() {
        return oneCriterion;
    }

    public void setOneCriterion(String oneCriterion) {
        this.oneCriterion = oneCriterion;
    }

    @Override
    public String toString() {
        return "EvaluationCriteria{" +
                "id=" + id +
                ", oneCriterion='" + oneCriterion + '\'' +
                '}';
    }
}
