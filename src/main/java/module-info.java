module com.example.javaapptm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop; //added this so i can use desktop.getDesktop()

    opens com.example.ListenUp to javafx.fxml;
    exports com.example.ListenUp;
}