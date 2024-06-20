package com.hansung.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

// Embeddable 클래스에 equals와 hashcode를 추가해야 set에 담겨있는 데이터의 비교가 가능해져 기능이 정상적으로 동작
@Embeddable
public class GrantedPermission {
    @Column(name = "perm")
    private String permission;
    private String grantor;

    protected GrantedPermission() {}

    public GrantedPermission(String permission, String grantor) {
        this.permission = permission;
        this.grantor = grantor;
    }

    public String getPermission() {
        return permission;
    }

    public String getGrantor() {
        return grantor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantedPermission that = (GrantedPermission) o;
        return Objects.equals(permission, that.permission) && Objects.equals(grantor, that.grantor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission, grantor);
    }

    @Override
    public String toString() {
        return "GrantedPermission{" +
                "permission='" + permission + '\'' +
                ", grantor='" + grantor + '\'' +
                '}';
    }
}

