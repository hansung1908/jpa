package com.hansung.main.ch1;

import com.hansung.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class UserSaveMain {
    public static void main(String[] args) {
        // persistence.xml에 정의한 영속 단위 기준으로 초기화, 필요 자원 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabegin");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = new User("user@user.com", "user", LocalDateTime.now());
            entityManager.persist(user);
            // 식별자를 직접 설정하는 경우 (해당 코드) commit을 시행하는 시점에서 insert 호출
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        emf.close();
    }
}
