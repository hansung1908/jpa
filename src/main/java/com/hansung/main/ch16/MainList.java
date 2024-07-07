package com.hansung.main.ch16;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.hansung.jpa.EMF;
import com.hansung.domain.SurveyQuestion;
import com.hansung.domain.Survey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainList {
    private static Logger logger = LoggerFactory.getLogger(MainList.class);

    public static void main(String[] args) {
        clearAll();
        EMF.init("jpabegin");
        try {
            createQuestions();
            createSurvey();
            addRemove();
            // removeAll();
        } finally {
            EMF.close();
        }
    }

    private static void createQuestions() {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new SurveyQuestion("Q1", "질문1"));
            em.persist(new SurveyQuestion("Q2", "질문2"));
            em.persist(new SurveyQuestion("Q3", "질문3"));
            em.persist(new SurveyQuestion("Q4", "질문4"));
            em.persist(new SurveyQuestion("Q5", "질문5"));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void createSurvey() {
        logger.info("createSurvey");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SurveyQuestion q1 = em.find(SurveyQuestion.class, "Q1");
            SurveyQuestion q2 = em.find(SurveyQuestion.class, "Q2");
            SurveyQuestion q3 = em.find(SurveyQuestion.class, "Q3");
            // 설문에 여러 질문들을 list로 묶어서 차례대로 저장
            List<SurveyQuestion> qs = new ArrayList<>();
            qs.add(q1);
            qs.add(q2);
            qs.add(q3);
            em.persist(new Survey("S1", "설문", qs));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void addRemove() {
        logger.info("addRemove");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SurveyQuestion q1 = em.find(SurveyQuestion.class, "Q1");
            SurveyQuestion q4 = em.find(SurveyQuestion.class, "Q4");
            Survey survey = em.find(Survey.class, "S1");
            // 설문에서 특정 질문을 추가하거나 삭제 (삭제 시 해당 질문은 null 처리)
            // 이때 추가나 삭제가 진행되면 순서번호(order_no)은 자동으로 업데이트
            survey.removeQuestion(q1);
            survey.addQuestion(q4);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void removeAll() {
        logger.info("removeAll");
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Survey survey = em.find(Survey.class, "S1");
            survey.removeAllQuestions();
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
            stmt.executeUpdate("truncate table survey_question");
            stmt.executeUpdate("truncate table survey");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

