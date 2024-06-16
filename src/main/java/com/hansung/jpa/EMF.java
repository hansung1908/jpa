package com.hansung.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// EntityManager 호출을 쉽게 하기 위한 클래스
public class EMF {
    private static EntityManagerFactory emf;

    public static void init(String className) {
        if (className.equals("ch4")) {
            emf = Persistence.createEntityManagerFactory("ch4");
        }
        else if (className.equals("ch5")) {
            emf = Persistence.createEntityManagerFactory("ch5");
        }
        else if (className.equals("oracle")){
            emf = Persistence.createEntityManagerFactory("oracle");
        }
        else if (className.equals("ch6")) {
            emf = Persistence.createEntityManagerFactory("ch6");
        }
        else if (className.equals("ch7")) {
            emf = Persistence.createEntityManagerFactory("ch7");
        }
        else {
            emf = Persistence.createEntityManagerFactory("jpabegin");
        }
    }

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
