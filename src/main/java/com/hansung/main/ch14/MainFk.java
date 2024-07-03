package com.hansung.main.ch14;

import com.hansung.domain.MembershipCard;
import com.hansung.domain.User;
import com.hansung.jpa.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainFk {
    private static Logger logger = LoggerFactory.getLogger(MainFk.class);

    public static void main(String[] args) {
        clearAll();
        EMF.init("jpabegin");
        try {
            saveUser();
            saveMembershipWithNullOwner();
            saveMembershipWithOwner();
            printMembership();
        } finally {
            EMF.close();
        }
    }

    private static void saveUser() {
        logger.info("saveUser");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new User("a@a.com", "A", LocalDateTime.now()));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void saveMembershipWithNullOwner() {
        logger.info("saveMembershipWithNullOwner");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MembershipCard card = new MembershipCard("1111000022223333",
                    null,
                    LocalDate.of(2024, 5, 31));
            em.persist(card);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void saveMembershipWithOwner() {
        logger.info("saveMembershipWithOwner");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, "a@a.com");
            MembershipCard card = new MembershipCard("8888111122223333",
                    user, // 객체를 매핑하여 a@a.com가 들어감
                    LocalDate.of(2024, 5, 31));
            em.persist(card);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void printMembership() {
        logger.info("printMembership");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // @OneToOne의 기본 패치설정이 eagar이므로 join을 통해 card와 owner의 정보를 같이 가져옴
            MembershipCard card = em.find(MembershipCard.class, "8888111122223333");
            logger.info("card: {}, {}", card.getCardNumber(), card.getExpiryDate());
            // lazy로 변경시 card 조회시 card만 가져오고 user 객체 사용시 owner 정보를 가져옴
            User owner = card.getOwner();
            logger.info("owner: {}", owner);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void clearAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jpa?serverTimezone=Asia/Seoul",
                "root",
                "1234");
             Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate("delete from membership_card where card_no != ''");
            stmt.executeUpdate("delete from user where email != ''");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
