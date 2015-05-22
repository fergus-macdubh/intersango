package com.luxoft.alpha.intersango.repository;

import com.luxoft.alpha.intersango.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u where username = :name")
    User getUserByUsername(@Param("name") String name);
}
