package com.hansung.main.ch17;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.jpa.EMF;
import com.hansung.domain.Player;
import com.hansung.domain.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MainCascade {
    private static Logger logger = LoggerFactory.getLogger(MainCascade.class);

    public static void main(String[] args) {
        clearAll();
        EMF.init("jpabegin");
        try {
            saveTeam();
            // removeTeam();
        } finally {
            EMF.close();
        }
    }

    private static void saveTeam() {
        logger.info("saveTeam");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // 엔티티 매니저를 통해 player를 찾아서 team에 추가하면 잘 작동하지만
            // player 객체를 새로 만들어 team에 추가하면 db에 player가 없어 team이 생성될 때 연관된 player 엔티티가 없다고 판단하여 예외 발생
            // 그래서 team 생성시 새로 만든 player도 같이 저장하려면 연관 매핑 관련 어노테이션에(ex. @OneToMany 등) cascade = CascadeType.PERSIST 옵션 추가
            // 추가하면 team을 insert할 때 연관된 player들도 같이 insert되어 예외 해결
            Player p21 = new Player("P-21", "선수21");
            Player p22 = new Player("P-22", "선수22");
            Set<Player> players = new HashSet<>();
            players.add(p21);
            players.add(p22);
            em.persist(new Team("T-01", "팀1", players));
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
            stmt.executeUpdate("truncate table team");
            stmt.executeUpdate("truncate table player");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

