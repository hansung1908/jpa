package com.hansung.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Subselect( // view처럼 쿼리 결과만 가지고 매핑하기 위한 어노테이션, 서브 쿼리 기능
        """
        select a.article_id, a.title, w.name as writer, a.written_at
        from article a left join writer w on a.writer_id = w.id
        """)
@Entity
@Immutable // @Subselect과 같이 사용됨
public class ArticleListView {
    @Id
    @Column(name = "article_id")
    private Long id;
    private String title;
    private String writer;
    @Column(name = "written_at")
    private LocalDateTime writtenAt;

    protected ArticleListView() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public LocalDateTime getWrittenAt() {
        return writtenAt;
    }

    @Override
    public String toString() {
        return "ArticleListView{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", writtenAt=" + writtenAt +
                '}';
    }
}

