module lk.ijse.gdse.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires java.desktop;
    requires net.sf.jasperreports.core;
    requires jdk.jfr;
    requires mysql.connector.j;
    requires com.google.protobuf;
    requires java.mail;

    opens lk.ijse.gdse.finalproject.dto.tm to javafx.base;
    opens lk.ijse.gdse.finalproject.controller to javafx.fxml;
    exports lk.ijse.gdse.finalproject;
}