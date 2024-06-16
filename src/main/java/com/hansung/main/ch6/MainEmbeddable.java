package com.hansung.main.ch6;

import com.hansung.domain.Address;
import com.hansung.domain.Grade;
import com.hansung.domain.Hotel;
import com.hansung.domain.Hotel2;
import com.hansung.jpa.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainEmbeddable {
    private static Logger logger = LoggerFactory.getLogger(MainEmbeddable.class);

    public static void main(String[] args) {
        EMF.init("ch6");
        saveHotel();
        printHotel();
        EMF.close();
    }

    private static void saveHotel() {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Address address = new Address("주소1", "주소2", "12345");
            Hotel2 hotel = new Hotel2("H00", "HN", 2022, Grade.S7, address);
            em.persist(hotel);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    private static void printHotel() {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Hotel2 hotel = em.find(Hotel2.class, "H00");
            if (hotel != null) {
                logger.info("주소: {}", hotel.getAddress());
            }
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }
    }
}

