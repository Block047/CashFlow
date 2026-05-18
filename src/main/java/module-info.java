module com.studytoolserver.cashflow {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson;

    opens com.studytoolserver.cashflow to javafx.fxml, com.google.gson;
    exports com.studytoolserver.cashflow;
}