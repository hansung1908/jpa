package com.hansung.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_member")
public class GameMember {
    @Id
    private String id;
    private String name;

    protected GameMember() {
    }

    public GameMember(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

