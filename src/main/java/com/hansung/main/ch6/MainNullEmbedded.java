package com.hansung.main.ch6;

import com.hansung.domain.Grade;
import com.hansung.domain.Hotel;
import com.hansung.domain.Hotel2;
import com.hansung.jpa.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainNullEmbedded {
    private static Logger logger = LoggerFactory.getLogger(MainNullEmbedded.class);

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
            em.persist(
                    new Hotel2("H009", "HN", 2022, Grade.S7, null)
            );
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    private static void printHotel() {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Hotel2 hotel = em.find(Hotel2.class, "H009");
            if (hotel != null) {
                logger.info("주소: {}", hotel.getAddress());
            }
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

