package com.example.blog.Services;

import com.example.blog.Models.Article;
import com.example.blog.repo.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.ofNullable;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Optional<Article> getById(Long id) {
        return articleRepository.findById(id);
    }

    public  List<Article> getByCategory(String category) {
        return articleRepository.getByCategory(category);
    }

    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    public List<Article> findByTitleContaining(String title) {
        return articleRepository.findByTitleContaining(title);
    }
    public List<Article> findByTitleContainingAndCategory(String title, String category) {
        return articleRepository.findByTitleContainingAndCategory(title, category);
    }
    public List<Article> findByCategory(String category) {
        return articleRepository.findByCategory(category);
    }

    public Article save(Article article) {
        Optional<Article> saved = ofNullable(articleRepository.save(article));
            return saved.get();
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

}

