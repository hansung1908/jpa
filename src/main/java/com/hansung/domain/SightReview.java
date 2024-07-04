package com.hansung.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "sight_review")
public class SightReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne // n-1 매핑 설정
    @JoinColumn(name = "sight_id") // 참조키 설정
    private Sight sight;
    private int grade;
    private String comment;

    protected SightReview() {
    }

    public SightReview(Sight sight, int grade, String comment) {
        this.sight = sight;
        this.grade = grade;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public Sight getSight() {
        return sight;
    }

    public int getGrade() {
        return grade;
    }

    public String getComment() {
        return comment;
    }
}

