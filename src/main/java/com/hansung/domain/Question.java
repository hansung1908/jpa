package com.hansung.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    private String id;
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "question_choice", // 매핑하려는 테이블 이름
            joinColumns = @JoinColumn(name = "question_id") // 매핑하려는 테이블의 기본키 칼럼
    )
    @OrderColumn(name = "idx") // 리스트로 생성시 인덱스 값을 만들기 위한 컬럼 지정
    @Column(name = "text") // 매핑하려는 테이블의 내용 칼럼
    private List<String> choices = new ArrayList<>();

    protected Question() {}

    public Question(String id, String text, List<String> choices) {
        this.id = id;
        this.text = text;
        this.choices = choices;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
