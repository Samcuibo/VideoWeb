
package com.webencyclop.demo.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "videos")
public class VideoSolr {


    @Id
    @Field
    private String title;

    @Field
    private String abstracts;

    @Field
    private String uploadBy;

    @Field
    private String videoAddress;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    @Override
    public String toString() {
        return "VideoSolr{" +
                "title='" + title + '\'' +
                ", abstracts='" + abstracts + '\'' +
                ", uploadBy='" + uploadBy + '\'' +
                ", videoAddress='" + videoAddress + '\'' +
                '}';
    }
}
