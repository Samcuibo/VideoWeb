package com.webencyclop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.webencyclop.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "from User where email = ?1")
    public User findUserByEmail(String email);

}

