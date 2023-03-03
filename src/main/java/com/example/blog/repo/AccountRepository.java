package com.example.blog.repo;

import com.example.blog.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
     Account findUserByUsername(String username);

     @Query("SELECT a FROM Account a WHERE a.username = :username")
     Optional<Account> findByUsername (String username);
     }

