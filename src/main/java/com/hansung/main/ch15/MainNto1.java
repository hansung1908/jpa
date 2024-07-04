package com.hansung.main.ch15;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import com.hansung.jpa.EMF;
import com.hansung.domain.SightReview;
import com.hansung.domain.Sight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MainNto1 {
    private static Logger logger = LoggerFactory.getLogger(MainNto1.class);

    public static void main(String[] args) {
        clearAll();
        EMF.init("jpabegin");
        try {
            saveSight();
            saveReview(5, "최고");
            saveReview(4, "좋음");
            printReview();
            printReviewsBySightId();
            printReviewsBySight();
        } finally {
            EMF.close();
        }
    }

    private static void saveSight() {
        logger.info("saveSight");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Sight sight = new Sight("S-01", "명소1");
            em.persist(sight);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void saveReview(int grade, String comment) {
        logger.info("saveReview");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Sight sight = em.find(Sight.class, "S-01");
            SightReview review = new SightReview(sight, grade, comment);
            em.persist(review);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void printReview() {
        logger.info("printReview");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // @ManyToOne의 패치설정이 eager이라 left join을 통해 sight와 sightreview 정보를 동시에 가져옴
            // lazy로 바꾸면 둘이 따로 가져오는게 가능
            logger.info("find(Review.class, 1L)");
            SightReview review = em.find(SightReview.class, 1L);
            logger.info("review.getSight()");
            Sight sight = review.getSight();
            logger.info("sight.getName()");
            String name = sight.getName();
            logger.info("name: {}", name);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void printReviewsBySightId() {
        logger.info("printReviews");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // jpql을 이용하여 특정 id를 조회
            TypedQuery<SightReview> query = em.createQuery("select r from SightReview r where r.sight.id = :sightId order by r.id desc", SightReview.class);
            query.setParameter("sightId", "S-01");
            List<SightReview> results = query.getResultList();
            results.forEach(r -> {
                Sight sight = r.getSight();
                logger.info("id: {}, sight: {}", r.getId(), sight.getName());
            });
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void printReviewsBySight() {
        logger.info("printReviews2");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // jpql을 이용해 특정 id를 조회하는데 객체를 통해 비교 조회
            Sight sight = em.find(Sight.class, "S-01");
            TypedQuery<SightReview> query = em.createQuery("select r from SightReview r where r.sight = :sight order by r.id desc", SightReview.class);
            query.setParameter("sight", sight);
            List<SightReview> results = query.getResultList();
            results.forEach(r -> {
                logger.info("id: {}, sight: {}", r.getId(), r.getSight().getName());
            });
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
            stmt.executeUpdate("truncate table sight_review");
            stmt.executeUpdate("truncate table sight");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

