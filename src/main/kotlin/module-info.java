module org.orons.dit.dbmslgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires java.sql;
    requires org.apache.commons.lang3;
    requires javafx.media;

    opens org.orons.dit.dbmslgame to javafx.fxml;
    exports org.orons.dit.dbmslgame;
}