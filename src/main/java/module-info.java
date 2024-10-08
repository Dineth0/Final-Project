module lk.ijse.gdse.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;

    opens lk.ijse.gdse.finalproject.dto.tm to javafx.base;
    opens lk.ijse.gdse.finalproject.controller to javafx.fxml;
    exports lk.ijse.gdse.finalproject;
}