package org.nmjava.chatapp.commons.daos;

import org.nmjava.chatapp.commons.models.Conservation;
import org.nmjava.chatapp.commons.utils.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ConservationDao {
    private final Optional<Connection> connection;

    public ConservationDao() {
        connection = ConnectDB.getConnection();
    }

    public Collection<Conservation> getListConservation(String userID) {
        Collection<Conservation> conservations = new ArrayList<>();

        String sql = "SELECT con.conservation_id, con.name, conupdate.last_message " +
                "FROM public.conservations_user conuser " +
                "join public.conservations con on con.conservation_id = conuser.conservation_id" +
                "join public.conservation_update conupdate on conupdate.conservation_id = con.conservation_id" +
                "WHERE user = ?" +
                "ORDER BY conupdate.last_update";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, userID);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String conservatioID = resultSet.getString("conservation_id");
                    String name = resultSet.getString("name");

                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return conservations;
    }
}
