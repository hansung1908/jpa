package com.hansung.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
public class AccessLog {
    @Id
    @TableGenerator(
            name = "accessIdGen",
            table = "id_seq",
            pkColumnName = "entity",
            pkColumnValue = "accesslog",
            valueColumnName = "nextval",
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

