package com.hansung.main.ch18;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import com.hansung.jpa.EMF;
import com.hansung.domain.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MainJpql {
    private static Logger logger = LoggerFactory.getLogger(MainJpql.class);

    public static void main(String[] args) {
        EMF.init("jpabegin");
        initData();
        try {
            selectReviewOrderBy();
        } finally {
            EMF.close();
        }
    }

    private static void selectReviewOrderBy() {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // TypedQuery를 통해 jpql을 생성
            TypedQuery<Review> query = em.createQuery(
                    "select r from Review r where r.hotelId = :hotelId order by r.id desc",
                    Review.class);
            query.setParameter("hotelId", "H-001");
            query.setFirstResult(8); // 0부터 시작
            query.setMaxResults(4);
            List<Review> reviews = query.getResultList();

            reviews.forEach(r -> logger.info("Review: {}", r.getId()));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void initData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jpa?serverTimezone=Asia/Seoul",
                "root",
                "1234");
             Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate("truncate table review");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new Review(5, "H-001", "작성자1", "댓글1"));
            em.persist(new Review(5, "H-001", "작성자2", "댓글2"));
            em.persist(new Review(5, "H-001", "작성자3", "댓글3"));
            em.persist(new Review(5, "H-001", "작성자4", "댓글4"));
            em.persist(new Review(5, "H-001", "작성자5", "댓글5"));
            em.persist(new Review(5, "H-001", "작성자6", "댓글6"));
            em.persist(new Review(5, "H-001", "작성자7", "댓글7"));
            em.persist(new Review(5, "H-001", "작성자8", "댓글8"));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

