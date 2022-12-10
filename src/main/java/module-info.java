module com.wms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports com.wms.controller;
    opens com.wms.controller to javafx.fxml;
    exports com.wms;
    opens com.wms to javafx.fxml;
}