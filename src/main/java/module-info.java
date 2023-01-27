module com.rafamoreno.clientechat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    opens com.rafamoreno.clientechat to javafx.fxml;
    exports com.rafamoreno.clientechat;
}