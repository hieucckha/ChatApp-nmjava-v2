module org.nmjava.chatapp.commons {
    requires static lombok;
    requires org.apache.commons.lang3;

    requires java.sql;
    requires jbcrypt;
    requires javafx.controls;

    exports org.nmjava.chatapp.commons.enums;
    exports org.nmjava.chatapp.commons.responses;
    exports org.nmjava.chatapp.commons.requests;
    exports org.nmjava.chatapp.commons.daos;
    exports org.nmjava.chatapp.commons.models;
    exports org.nmjava.chatapp.commons.utils;
}