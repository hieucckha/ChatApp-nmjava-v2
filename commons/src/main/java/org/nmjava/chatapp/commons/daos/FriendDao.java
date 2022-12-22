package org.nmjava.chatapp.commons.daos;

import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.utils.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class FriendDao {
    private final Optional<Connection> connection;

    public FriendDao() {
        connection = ConnectDB.getConnection();
    }

    public Collection<Friend> getListFriend(String userID) {
        Collection<Friend> friends = new ArrayList<>();

        String sql = "SELECT friends.friend_id, users.username " + "FROM public.friends  join public.users on friends.friend_id = users.user_id " + "WHERE friends.user_id = ? and friends.is_friend = true";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, userID);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendID = resultSet.getString("friend_id");
                    String username = resultSet.getString("username");

                    friends.add(new Friend(friendID, username));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return friends;
    }

    public Collection<Friend> getListFriendOnline(String userID) {
        Collection<Friend> friends = new ArrayList<>();

        String sql = "SELECT friends.friend_id, users.username " + "FROM public.friends  join public.users on friends.friend_id = users.user_id " + "WHERE friends.user_id = ? and users.is_online = true";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, userID);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendID = resultSet.getString("friend_id");
                    String username = resultSet.getString("username");

                    friends.add(new Friend(friendID, username));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return friends;
    }

    public Collection<Friend> getListRequestFriend(String userID) {
        Collection<Friend> friends = new ArrayList<>();

        String sql = "SELECT friends.friend_id, users.username " + "FROM public.friends  join public.users on friends.friend_id = users.user_id " + "WHERE friends.user_id = ? and friends.is_friend = false";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, userID);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendID = resultSet.getString("friend_id");
                    String username = resultSet.getString("username");

                    friends.add(new Friend(friendID));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return friends;
    }

    public Optional<Boolean> acceptFriend(String userID, String friendID) {
        String sql = "UPDATE public.friends " + "SET is_friend = true " + "WHERE user_id = ? and friend_id = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, userID);
                statement.setString(2, friendID);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }
}
