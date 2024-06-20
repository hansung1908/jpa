package com.hansung.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    private String id;
    private String name;

    @ElementCollection // 조회시 eager 방식을 사용하고 싶으면 (fetch = FetchType.EAGER), 기본은 lazy방식
    @CollectionTable( // role_perm 테이블의 role_id를 참조 (join)
            name = "role_perm",
            joinColumns = @JoinColumn(name = "role_id")
    )
    @Column(name = "perm") // 실제 값을 갖는 컬럼
    private Set<String> permissions = new HashSet<>();

    protected Role() {}

    public Role(String id, String name, Set<String> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void revokeAll() {
        this.permissions.clear(); // SELECT -> DELETE
        // this.permissions = new HashSet<>(); // DELETE
    }

    public void setPermissions(Set<String> newPermissions) {
        this.permissions = newPermissions;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}

