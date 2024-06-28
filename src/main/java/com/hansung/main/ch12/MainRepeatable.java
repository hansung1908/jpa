package com.hansung.main.ch12;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.jpa.EMF;
import com.hansung.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class MainRepeatable {
    private static Logger logger = LoggerFactory.getLogger(MainRepeatable.class);

    public static void main(String[] args) {
        EMF.init("jpabegin");
        clearAll();
        saveUser("a@a.com", "name");
        readTwice("a@a.com");
        EMF.close();
    }

    private static void saveUser(String email, String name) {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new User(email, name, LocalDateTime.now()));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void readTwice(String email) {
        logger.info("readTwice");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // 처음 실행시 쿼리를 통해 해당 객체를 가져오지만
            // 두번째는 첫 쿼리 실행을 통해 얻은 객체가 영속 컨텍스트에 저장되어
            // 해당 객체를 가져오므로 두번째 쿼리는 실행되지 않고 객체를 가져옴
            logger.info("first find");
            User first = em.find(User.class, email);
            logger.info("second find");
            User second = em.find(User.class, email);
            logger.info("same object: {}", (first == second));
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
            stmt.executeUpdate("delete from user where email != ''");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

