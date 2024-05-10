package com.api.spring.repository;

import com.api.spring.model.Book;
import com.api.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT User AS u FROM User WHERE u.userName =:userName")
    User findByUsername(@Param("userName") String userName);
}
