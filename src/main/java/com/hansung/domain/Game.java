package com.hansung.domain;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "game")
public class Game {
    @Id
    private String id;
    private String name;
    @OneToMany // 1-n 연관 매핑
    @JoinColumn(name = "game_id")
    @MapKeyColumn(name = "role_name") // 키 값을 위한 컬럼 지정
    private Map<String, GameMember> members = new HashMap<>();

    protected Game() {
    }

    public Game(String id, String name, Map<String, GameMember> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public void add(String role, GameMember member) {
        members.put(role, member);
    }

    public void remove(String role) {
        members.remove(role);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, GameMember> getMembers() {
        return members;
    }
}

