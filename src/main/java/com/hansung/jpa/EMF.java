package com.hansung.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// EntityManager 호출을 쉽게 하기 위한 클래스
public class EMF {
    private static EntityManagerFactory emf;

    public static void init(String className) {
        if (className.equals("jpabegin")) {
            emf = Persistence.createEntityManagerFactory("jpabegin");
        }
        else if (className.equals("oracle")){
            emf = Persistence.createEntityManagerFactory("oracle");
        }
        else {
            System.out.println("persistence 매핑 확인!!!");
        }
    }

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
