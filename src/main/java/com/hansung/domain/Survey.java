package com.hansung.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
public class Survey {
    @Id
    private String id;
    private String name;
    @OneToMany // 1-n 매핑 설정
    @JoinColumn(name = "survey_id")
    @OrderColumn(name = "order_no") // 순서를 정하기 위한 컬럼 지정
    private List<SurveyQuestion> questions = new ArrayList<>();

    protected Survey() {
    }

    public Survey(String id, String name, List<SurveyQuestion> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

    public void addQuestion(SurveyQuestion q) {
        this.questions.add(q);
    }

    public void removeQuestion(SurveyQuestion q) {
        this.questions.remove(q);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    public void removeAllQuestions() {
        questions.clear();
    }
}

