package com.hansung.domain;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "doc")
public class Document {
    @Id
    private String id;
    private String title;
    private String content;
    @ElementCollection
    @CollectionTable(
            name = "doc_prop", // 매핑할 테이블 이름
            joinColumns = @JoinColumn(name = "doc_id") // 매핑할 테이블의 기본키
    )
    @MapKeyColumn(name = "name") // 매핑할 테이블에서 키값으로 쓰일 컬럼 설정
    @Column(name = "value") // 밸류로 쓰일 컬럼
    private Map<String, String> props = new HashMap<>();

    protected Document() {}

    public Document(String id, String title, String content, Map<String, String> props) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.props = props;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setProp(String name, String value) {
        props.put(name, value);
    }

    public void removeProp(String name) {
        props.remove(name);
    }
}

