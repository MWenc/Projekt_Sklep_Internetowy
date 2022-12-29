module com.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires java.sql;
    requires java.persistence;
    requires commons.validator;

    opens com.projekt.Tabele to org.hibernate.orm.core, javafx.base;
    opens com.projekt.sceny to javafx.fxml;
    exports com.projekt;
    exports com.projekt.sceny ;
    exports com.projekt.Tabele ;
    opens com.projekt to javafx.base, javafx.fxml, org.hibernate.orm.core;
}