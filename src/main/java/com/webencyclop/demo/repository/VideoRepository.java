package com.webencyclop.demo.repository;

import com.webencyclop.demo.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

    //    @Query(value = "from Video where videoAddress = ?1")
    public Video getVideoByVideoAddress(String address);
}

