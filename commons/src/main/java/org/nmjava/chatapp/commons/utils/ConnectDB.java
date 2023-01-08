package org.nmjava.chatapp.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ConnectDB {
    private static Optional<Connection> connection = Optional.empty();

    public static Optional<Connection> getConnection() {
        if (connection.isEmpty()) {
            Properties prop = new Properties();

            try (InputStream resourceAsStream = ConnectDB.class.getClassLoader().getResourceAsStream("db.properties")) {
                prop.load(resourceAsStream);
            } catch (IOException ioEx) {
                ioEx.printStackTrace(System.err);
            }

            prop.forEach((k, v) -> {
                System.out.println(k + " - " + v);
            });

            String driverClassName = prop.getProperty("db.driverClassName");
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String pass = prop.getProperty("db.password");

            try {
                Class.forName("org.postgresql.Driver");
                connection = Optional.ofNullable(DriverManager.getConnection(url, user, pass));
                System.out.println("success");

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace(System.err);
                System.exit(0);
            }
        }

        return connection;
    }
}
