package com.hansung.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.domain.User;
import com.hansung.jpa.EMF;

// 새로운 유저 정보 저장하는 서비스 클래스
public class NewUserService {

    public void saveNewUser(User user) {

        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(user);

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

