module com.example.sofcprojekt2kunz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.sofcprojekt2kunz to javafx.fxml;
    exports com.sofcprojekt2kunz;
}