package com.projekt;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 * Klasa inicjalizująca configuracje Hibernate'a.
 * @author Michał Wenc
 * @version 1.0
 */
public class initHibernate {
    public static Configuration configuration;
    public static SessionFactory sessionFactory;

    /**
     * Metoda ta inicjaliuje configurację hibernate oraz tworząca sesje z bazą danych
     */
    public static void init() {
        configuration = new Configuration().configure("hibernate.cfg.xml");

        sessionFactory = configuration.buildSessionFactory();

    }

}
