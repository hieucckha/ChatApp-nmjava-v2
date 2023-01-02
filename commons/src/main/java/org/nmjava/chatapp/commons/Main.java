package org.nmjava.chatapp.commons;

import org.mindrot.jbcrypt.BCrypt;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(BCrypt.hashpw("123", BCrypt.gensalt()));
    }
}