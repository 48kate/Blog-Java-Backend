package com.example.blog.Controllers;
import com.example.blog.Models.Article;
import com.example.blog.Services.ArticleService;
import com.example.blog.repo.ArticleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
//@RestController комбинация аннотаций @Controller и @ResponseBody, которые всегда используются для создания restfull сервисов
//@ResponseBody используется для конвертации объекта java в json
public class ArticleController {

    @PersistenceContext
    private EntityManager entityManager;
    private ArticleService articleService;
    private ArticleRepository articleRepository;

    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }
   // @PreAuthorize("isAuthenticated()")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> findArticle(@PathVariable Long id) {
       Optional<Article> article = articleService.getById(id);
       if (article.isPresent()) {
           return new ResponseEntity<>(article.get(), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @PostMapping("/add")
    //@PreAuthorize("isAuthenticated()")
    // create article rest api
    public ResponseEntity<Article> createArticle(@RequestBody @Valid Article article) {
        try {
            Article saved = articleService.save(article);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //@RequestBody используется для получения JSON из HTTP запроса и конвертации его в java объект
    //@PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @PutMapping("/edit/{id}")
    //http://localhost:8080/articles?id=id - query parameter
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        Optional<Article> articleData =articleService.getById(id);

        if (articleData.isPresent()) {
            Article updated = articleData.get();
            updated.setTitle(article.getTitle());
            updated.setAuthor(article.getAuthor());
            updated.setFull_text(article.getFull_text());
            updated.setCategory(article.getCategory());
            updated.setImg(article.getImg());
            return new ResponseEntity<>(articleService.save(updated), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //@RequestParam извлекает значение query пар-ра из URL запроса
   // @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteById(id);
            return new ResponseEntity<>("Post was deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //@PathVariable указывает на то, что данный параметр получается из адресной строки
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @GetMapping("/articles")
    //get all articles rest api
    public ResponseEntity<List<Article>> findArticlesByTitle(
            @RequestParam(required = false) Optional<String> title,
            @RequestParam(required = false) Optional<String> category
    ){
        try {
            List<Article> articles;
            System.out.println(title);
            if (title.isPresent() && category.isPresent()) {
                articles = articleService.findByTitleContainingAndCategory(title.get(), category.get());
            }
            else if (title.isPresent()) {
                articles = articleService.findByTitleContaining(title.get());
            }
            else if (category.isPresent()) {
                articles = articleService.findByCategory(category.get());
            }
            else {
                articles = articleService.getAll();
            }

            if (articles.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
