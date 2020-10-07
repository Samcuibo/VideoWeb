package com.webencyclop.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "rating_criteria")
public class RatingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criterion_id")
    private int id;


    @Column(name = "one_criterion")
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

    public void setOneCriterion(String onCriterion) {
        this.oneCriterion = onCriterion;
    }


    @Override
    public String toString() {
        return "RatingCriteria{" +
                "id=" + id +
                ", onCriterion='" + oneCriterion + '\'' +
                '}';
    }
}
