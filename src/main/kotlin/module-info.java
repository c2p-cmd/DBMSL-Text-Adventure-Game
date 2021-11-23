module org.orons.dit.dbmslgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires java.sql;

    opens org.orons.dit.dbmslgame to javafx.fxml;
    exports org.orons.dit.dbmslgame;
}