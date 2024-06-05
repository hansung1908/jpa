package com.hansung.app;

import jakarta.persistence.EntityManager;
import com.hansung.domain.User;
import com.hansung.jpa.EMF;

// 저장된 유저 정보를 불러오는 서비스 클래스
// 이때, 저장된 유저 없는 경우에 대한 예외 처리 필수
public class GetUserService {

    public User getUser(String email) {

        EntityManager em = EMF.createEntityManager();

        try {
            // 엔티티 타입(User.class)와 id 타입(email)이 일치해야 함
            // 일치하지 않으면 exception
            User user = em.find(User.class, email);

            if (user == null) {
                throw new NoUserException();
            }

            return user;
        } finally {
            em.close();
        }
    }
}
