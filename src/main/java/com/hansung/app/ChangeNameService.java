package com.hansung.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.domain.User;
import com.hansung.jpa.EMF;

// 저장된 유저에서 이름을 바꾸는 서비스 클래스
public class ChangeNameService {

    public void changeName(String email, String newName) {

        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User user = em.find(User.class, email);

            if (user == null) {
                throw new NoUserException();
            }

            user.changeName(newName);
            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

