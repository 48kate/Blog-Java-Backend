package com.example.blog.Models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

// отвечает за хранение всех статей
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Article{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String author;
    @Column(columnDefinition = "TEXT")
    private String full_text;
    private String category;
    private String img;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Article(String title, String author, String full_text, String category, String img) {
        this.title = title;
        this.author = author;
        this.full_text = full_text;
        this.category = category;
        this.img = img;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", author='" + author + "'" +
                ", full_text='" + full_text + "'" +
                ", category='" + category + "'" +
                ", img='" + img + "'" +
                "}";
    }
}
