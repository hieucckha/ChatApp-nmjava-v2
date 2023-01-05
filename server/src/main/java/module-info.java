module org.nmjava.chatapp.server {
    requires static lombok;
    requires org.apache.commons.lang3;
    requires java.mail;

    requires org.nmjava.chatapp.commons;

    exports org.nmjava.chatapp.server;
    exports org.nmjava.chatapp.server.app;
}