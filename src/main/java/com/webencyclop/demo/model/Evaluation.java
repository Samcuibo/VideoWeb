package com.webencyclop.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "video_evaluation")
public class Evaluation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private int id;

    @Column(name = "video_address")
    private String videoAddress;

    @Column(name = "rated_by")
    private String ratedBy;


    @Column(name = "criterion")
    private String criterion;

    @Column(name = "performance")
    private String level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public String getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(String ratedBy) {
        this.ratedBy = ratedBy;
    }





    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    @Override
    public String toString() {
        return "Evaluation{" +
                "id=" + id +
                ", videoAddress='" + videoAddress + '\'' +
                ", ratedBy='" + ratedBy + '\'' +
                ", attribute='" + criterion + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

}
