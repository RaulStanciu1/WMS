module com.wms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    exports com.wms.controller;
    opens com.wms.controller to javafx.fxml;
    exports com.wms;
    opens com.wms to javafx.fxml;
    exports com.wms.data;
    opens com.wms.data to javafx.fxml;
    exports com.wms.data.models;
    opens com.wms.data.models to javafx.fxml;
    exports com.wms.data.misuse;
    opens com.wms.data.misuse to javafx.fxml;
    exports com.wms.data.analysis;
    opens com.wms.data.analysis to javafx.fxml;
}