package com.webencyclop.demo.repository;

import com.webencyclop.demo.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(value = "from Rating where videoAddress = ?1 and ratedBy = ?2 and attribute = ?3")
    public Rating findByVideoAddressUserAndAttribute (String Address,String User,String attribute);


    @Query(value = "select * from video_rating where video_address = ?1 and attribute =?2", nativeQuery = true)
    public List<Rating> findAllByVideoAddressAndAttribute( String address,String attribute);



}
