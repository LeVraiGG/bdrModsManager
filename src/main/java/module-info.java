module bdr.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires com.google.common;


    exports bdr.projet;
    opens bdr.projet;
}