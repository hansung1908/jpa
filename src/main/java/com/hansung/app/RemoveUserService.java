package com.hansung.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.domain.User;
import com.hansung.jpa.EMF;

// 저장된 유저를 삭제하는 서비스 클래스
public class RemoveUserService {

    public void removeUser(String email) {

        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User user = em.find(User.class, email);

            if (user == null) {
                throw new NoUserException();
            }

            em.remove(user);
            // 커밋전 이시점에서 다른 프로세스가 해당 데이터를 삭제하면 exception 발생
            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

