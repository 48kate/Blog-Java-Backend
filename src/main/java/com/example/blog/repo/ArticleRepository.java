package com.example.blog.repo;

import com.example.blog.Models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


//репозиторий для модели Article
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleContaining(String title);
    List<Article> findByTitleContainingAndCategory(String title, String category);
    @Query("SELECT a FROM Article a WHERE a.category = :category")
    List<Article> findByCategory (String category);

    List<Article> getByCategory (String category);




}
