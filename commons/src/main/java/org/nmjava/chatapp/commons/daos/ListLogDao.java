package org.nmjava.chatapp.commons.daos;

import org.nmjava.chatapp.commons.models.modelLoginList;
import org.nmjava.chatapp.commons.utils.ConnectDB;
import java.sql.*;
import java.util.*;

public class ListLogDao {
    private final Optional<Connection> connection;

    public ListLogDao() {
        connection = ConnectDB.getConnection();
    }

    public Collection<modelLoginList> getInfoAll() {
        Collection<modelLoginList> loginLists = new ArrayList<>();
        String sql = "SELECT username, full_name, create_at " +
                "FROM public.users";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String full_name = resultSet.getString("full_name");
                    String create_at = resultSet.getString("create_at");

                    modelLoginList loginList = new modelLoginList(username, full_name, create_at);
                    loginLists.add(loginList);
                }
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });
        return loginLists;
    }

    public Collection<modelLoginList> getListLog(String username) {
        Collection<modelLoginList> modelLoginLists = new ArrayList<>();

        String sql = "select login_at from login_logs join users " +
                "on login_logs.username = users.username " +
                "where users.username = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String login_at = resultSet.getString("login_at");

                    modelLoginList loginList= new modelLoginList("","", login_at);
                    modelLoginLists.add(loginList);
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });
        return modelLoginLists;
    }
}
