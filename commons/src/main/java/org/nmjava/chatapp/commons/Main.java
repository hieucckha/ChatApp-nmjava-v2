package org.nmjava.chatapp.commons;

import org.mindrot.jbcrypt.BCrypt;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println(BCrypt.hashpw("123", BCrypt.gensalt()));
        System.out.println(BCrypt.checkpw("123", "$2a$10$Gpf3cPOijkumLLLSx28OUOR0Hx2UMlwOH9YCuntsOAHwXsDYxEEh2"));
        System.out.println(BCrypt.checkpw("123", "$2a$10$pA8bMyzSc0p.O8X99xDl4.sWaauta3YYhOzrYkYVynx2dmcJpZt0W"));
        System.out.println(BCrypt.checkpw("123", "$2a$10$Ham.W3lXq7XzLuEMRBvPZ.q6.3ep63hE4dTGU1e8lK3C8DzFMrs9K"));
    }
}