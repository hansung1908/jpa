package com.hansung.main.ch5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.domain.AccessLog;
import com.hansung.jpa.EMF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class MainTableGenId {
    private static Logger logger = LoggerFactory.getLogger(MainTableGenId.class);

    public static void main(String[] args) {
        EMF.init("jpabegin");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            AccessLog log = new AccessLog("/path01", LocalDateTime.now());
            logger.info("persist 실행 전");
            em.persist(log);
            logger.info("persist 실행 함");
            logger.info("생성한 식별자: {}", log.getId());
            logger.info("커밋하기 전");
            tx.commit();
            logger.info("커밋함");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        EMF.close();
    }
}

