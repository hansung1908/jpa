package com.hansung.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "crm", name = "activity_log")
public class ActivityLog {
    @Id
    @SequenceGenerator(
            name = "log_seq_gen",
            sequenceName = "activity_seq",
            schema = "crm",
            allocationSize = 1 // 1 이외에 다른 숫자로 설정하면 불필요한 생성자가 여러개 생성될 수 있음
    )
    @GeneratedValue(generator = "log_seq_gen")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "activity_type")
    private String activityType;

    private LocalDateTime created;

    protected ActivityLog() {
    }

    public ActivityLog(String userId, String activityType) {
        this.userId = userId;
        this.activityType = activityType;
        this.created = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}

