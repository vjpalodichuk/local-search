module com.capital7software.ai.localsearch {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.base;

    opens com.capital7software.ai.localsearch to javafx.fxml;

    exports com.capital7software.ai.localsearch;
}