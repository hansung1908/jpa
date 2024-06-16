package com.hansung.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
public class AccessLog {
    @Id
    @TableGenerator(
            name = "accessIdGen", // 코드 내에서 사용하는 테이블 이름
            table = "id_seq", // db에 있는 실제 테이블 이름
            pkColumnName = "entity", // primary key 컬럼 이름
            pkColumnValue = "accesslog", // pk 컬럼 내에서 해당 이름을 가진 데이터 설정 (key)
            valueColumnName = "nextval", // 설정한 데이터와 매칭되는 값이 있는 컬럼 이름 (value)
            initialValue = 0,
            allocationSize = 1 // 1 이외에 다른 숫자로 설정하면 불필요한 생성자가 여러개 생성될 수 있음
    )
    @GeneratedValue(generator = "accessIdGen")
    private Long id;

    private String path;

    private LocalDateTime accessed;

    protected AccessLog() {
    }

    public AccessLog(String path, LocalDateTime accessed) {
        this.path = path;
        this.accessed = accessed;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getAccessed() {
        return accessed;
    }
}

