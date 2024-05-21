package com.hansung;

import com.hansung.entitiy.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("customer-exam");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        // 동작은 이 사이에서 진행
        em.persist(Customer.sample());


        tx.commit(); // tx.rollback();
        em.close();
        emf.close();
    }
}