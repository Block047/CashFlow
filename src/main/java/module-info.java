module com.studytoolserver.cashflow {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson;

    opens com.block047.cashflow to javafx.fxml, com.google.gson;
    exports com.block047.cashflow;
}