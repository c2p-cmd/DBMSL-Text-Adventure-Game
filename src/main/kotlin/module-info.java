module org.orons.dit.dbmslgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires org.junit.jupiter.api;
    requires javafx.media;

    opens org.orons.dit.dbmslgame to javafx.fxml;
    exports org.orons.dit.dbmslgame;
}