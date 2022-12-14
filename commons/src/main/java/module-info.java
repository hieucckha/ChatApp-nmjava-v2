module org.nmjava.chatapp.commons {
    requires static lombok;
    requires org.apache.commons.lang3;

    exports org.nmjava.chatapp.commons.enums;
    exports org.nmjava.chatapp.commons.responses;
    exports org.nmjava.chatapp.commons.requests;
}