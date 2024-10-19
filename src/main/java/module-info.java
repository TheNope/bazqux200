module com.thenope.bazqux200 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens com.thenope.bazqux200.config to com.fasterxml.jackson.databind;
    opens com.thenope.bazqux200 to javafx.fxml;
    exports com.thenope.bazqux200;
    opens com.thenope.bazqux200.config.classes to com.fasterxml.jackson.databind;
    exports com.thenope.bazqux200.music;
    opens com.thenope.bazqux200.music to javafx.fxml;
    exports com.thenope.bazqux200.util;
    opens com.thenope.bazqux200.util to javafx.fxml;
}