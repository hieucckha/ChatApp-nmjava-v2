module org.nmjava.chatapp.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.base;

    requires java.sql;
    requires org.postgresql.jdbc;
    requires jbcrypt;

    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    requires org.nmjava.chatapp.commons;
    requires org.apache.commons.lang3;

    exports org.nmjava.chatapp.client;
    opens org.nmjava.chatapp.client to javafx.fxml;

    exports org.nmjava.chatapp.client.components;
    opens org.nmjava.chatapp.client.components to javafx.fxml;
    exports org.nmjava.chatapp.client.controllers;
    opens org.nmjava.chatapp.client.controllers to javafx.fxml;
    exports org.nmjava.chatapp.client.utils;
    opens org.nmjava.chatapp.client.utils to javafx.fxml;
}