package com.hansung.main.ch16;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.domain.Game;
import com.hansung.domain.GameMember;
import com.hansung.jpa.EMF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MainMap {
    private static Logger logger = LoggerFactory.getLogger(MainMap.class);

    public static void main(String[] args) {
        clearAll();
        EMF.init("jpabegin");
        try {
            addMember();
            addGameWithMembers();
            addRemove();
        } finally {
            EMF.close();
        }
    }

    private static void addMember() {
        logger.info("addMember");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new GameMember("M1", "멤버1"));
            em.persist(new GameMember("M2", "멤버2"));
            em.persist(new GameMember("M3", "멤버3"));
            em.persist(new GameMember("M4", "멤버4"));
            em.persist(new GameMember("M5", "멤버5"));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void addGameWithMembers() {
        logger.info("addGameWithMembers");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            GameMember m1 = em.find(GameMember.class, "M1");
            GameMember m2 = em.find(GameMember.class, "M2");
            GameMember m3 = em.find(GameMember.class, "M3");
            // 게임에 참가할 게임 멤버를 map으로 묶어서 저장
            Map<String, GameMember> members = new HashMap<>();
            members.put("C", m1);
            members.put("PG", m2);
            members.put("SG", m3);
            Game g = new Game("G1", "게임1", members);
            em.persist(g);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void addRemove() {
        logger.info("addRemove");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Game g = em.find(Game.class, "G1");
            GameMember m4 = em.find(GameMember.class, "M4");
            GameMember m5 = em.find(GameMember.class, "M5");
            g.add("C", m4); // 기존 키 값 'C'에 m4 매핑
            g.add("PF", m5); // 신규 키 값 'PF'에 추가
            g.remove("SG"); // 키 값 'SG' 삭제
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void clearAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jpa?serverTimezone=Asia/Seoul",
                "root",
                "1234");
             Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate("truncate table game_member");
            stmt.executeUpdate("truncate table game");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

