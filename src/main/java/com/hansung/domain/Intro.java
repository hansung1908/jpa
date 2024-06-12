package com.hansung.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Intro {
    @Column(table = "writer_intro", name = "content_type") // 테이블 이름 명시
    private String contentType;
    @Column(table = "writer_intro") // 테이블 이름 명시
    private String content;

    protected Intro() {
    }

    public Intro(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Intro{" +
                "contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

