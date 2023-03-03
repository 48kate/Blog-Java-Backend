package com.example.blog.Services;
import com.example.blog.Models.Account;
import com.example.blog.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

//Содержит методы для бизнес-логики приложения, реализует интерфейс UserDetailsService
@Service
public class AccountService implements UserDetailsService {


    @Autowired
    private AccountRepository accountRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        try {
            System.out.println(username);
            // WHERE string SQL injection
            Query q = entityManager.createNativeQuery("SELECT * FROM `account` WHERE username = '" + username +"'", Account.class);
            account = (Account) q.getSingleResult();
            System.out.println(account);
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        if (account == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return account;
    }


    public UserDetails loadUserByUsernameAndPassword(String username, String encodedPassword) throws UsernameNotFoundException {
        Account account;
        try {
            System.out.println(username);
            // WHERE string SQL injection
            Query q = entityManager.createNativeQuery("SELECT * FROM `account` WHERE username = '" + username + "' AND password = '" + encodedPassword + "'", Account.class);
            account = (Account) q.getSingleResult();
            System.out.println(account);
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        if (account == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return account;
    }
    public Account getByUsername(String username) {
        Account account = accountRepository.findUserByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return account;
    }

    public Account findUserById(Long accountId) {
        Optional<Account> accountFromDb = accountRepository.findById(accountId);
        return accountFromDb.orElse(new Account());
    }

    public List<Account> allUsers() {
        return accountRepository.findAll();
    }
    public boolean deleteUser(Long accountId) {
        if (accountRepository.findById(accountId).isPresent()) {
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

}


