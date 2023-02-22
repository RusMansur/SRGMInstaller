module com.rusmansur.srgminstaller {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires java.desktop;

    opens com.rusmansur.srgminstaller to javafx.fxml;
    opens com.rusmansur.srgminstaller.controllers to javafx.fxml;

    exports com.rusmansur.srgminstaller;
    exports com.rusmansur.srgminstaller.controllers;
}