package com.hansung.main.ch5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.domain.Review;
import com.hansung.jpa.EMF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainNative {
    private static Logger logger = LoggerFactory.getLogger(MainNative.class);

    public static void main(String[] args) {
        EMF.init("jpabegin");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Review review = new Review(5, "H-01", "작성자", "댓글");
            logger.info("persist 실행 전");
            em.persist(review);
            logger.info("persist 실행 함");
            logger.info("생성한 식별자: {}", review.getId());
            logger.info("커밋하기 전");
            tx.commit();
            logger.info("커밋함");
        } catch (Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }
        EMF.close();
    }
}

