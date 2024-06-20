package com.hansung.main.ch6;

import com.hansung.domain.Address;
import com.hansung.domain.Employee;
import com.hansung.jpa.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainAttrOver {
    private static Logger logger = LoggerFactory.getLogger(MainAttrOver.class);

    public static void main(String[] args) {
        EMF.init("jpabegin");
        saveEmployee();
        EMF.close();
    }

    private static void saveEmployee() {
        EntityManager em = EMF.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Address home = new Address("집주소1", "집주소2", "12345");
            Address work = new Address("직장주소1", "직장주소2", "68890");
            Employee employee = new Employee("EMP01", home, work);
            em.persist(employee);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

}

