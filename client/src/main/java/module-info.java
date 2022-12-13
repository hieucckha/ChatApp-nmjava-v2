module org.nmjava.chatapp.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.postgresql.jdbc;
    requires jbcrypt;

    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    exports org.nmjava.chatapp.client;
    opens org.nmjava.chatapp.client to javafx.fxml;

    exports org.nmjava.chatapp.client.components;
    opens org.nmjava.chatapp.client.components to javafx.fxml;
    exports org.nmjava.chatapp.client.controllers;
    opens org.nmjava.chatapp.client.controllers to javafx.fxml;
    exports org.nmjava.chatapp.client.daos;
    opens org.nmjava.chatapp.client.daos to javafx.fxml;
    exports org.nmjava.chatapp.client.models;
    opens org.nmjava.chatapp.client.models to javafx.fxml;
    exports org.nmjava.chatapp.client.utils;
    opens org.nmjava.chatapp.client.utils to javafx.fxml;
}