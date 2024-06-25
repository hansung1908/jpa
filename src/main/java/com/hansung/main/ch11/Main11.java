package com.hansung.main.ch11;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import com.hansung.jpa.EMF;
import com.hansung.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

public class Main11 {
    private static Logger logger = LoggerFactory.getLogger(Main11.class);

    public static void main(String[] args) {
        EMF.init("jpabegin");
        clearAll();
        for (int i = 11 ; i <= 20 ; i++) {
            saveRole("R" + i, "name" + i);
        }
        selectNoFetchMode();
        selectFetchMode();
        EMF.close();
    }

    private static void clearAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jpa?serverTimezone=Asia/Seoul",
                "root",
                "1234");
             Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate("delete from role_perm where role_id != ''");
            stmt.executeUpdate("delete from role where id != ''");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveRole(String roleId, String name) {
        logger.info("saveRole");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Role role = new Role(roleId, name, Set.of("F1", "F2"));
            em.persist(role);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    private static void selectNoFetchMode() {
        logger.info("selectNoFetchMode");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // 해당 코드들을 실행할때 permissions는 eager fetch를 했지만
            // 먼저 role에서 조회 (limit 조건 추가)하고 이후에 role_perm을 조회 (role의 갯수 만큼)
            // eager fetch와는 상반되는 결과를 초래
            TypedQuery<Role> query = em.createQuery(
                    "select r from Role r where r.name like :name order by r.id", Role.class);
            query.setParameter("name", "name%");
            query.setFirstResult(6);
            query.setMaxResults(3);
            List<Role> roles = query.getResultList();
            roles.forEach(r -> {
                logger.info("role: id={} name={} perms={}", r.getId(), r.getName(), r.getPermissions().size());
            });
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    private static void selectFetchMode() {
        logger.info("selectFetchMode");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // sql에서 join fetch를 할 경우 join을 통해 같이 불어올 수 있으나
            // 페이징 처리를 위한 first, max (limit)처리를 했으나 적용되지 않고 조건에 맞는 모든 데이터를 다 조회 후
            // 메모리에서 페이징 처리를 실행함 (applying in memory 라는 로그가 찍힘)
            // 데이터 양이 적으면 괜찮지만 양이 많으면 out of memory (메모리 부족) 발생
            TypedQuery<Role> query = em.createQuery(
                    "select r from Role r left join fetch r.permissions where r.name like :name order by r.id", Role.class);
            query.setParameter("name", "name%");
            query.setFirstResult(6);
            query.setMaxResults(3);
            List<Role> roles = query.getResultList();
            roles.forEach(r -> {
                logger.info("role: id={} name={} perms={}", r.getId(), r.getName(), r.getPermissions().size());
            });
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

