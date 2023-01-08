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

    public Collection<Friend> getListFriend(String username) {
        Collection<Friend> friends = new ArrayList<>();

        String sql = "SELECT friend_username " +
                "FROM public.friends " +
                "WHERE user_username = ? and is_friend = true " +
                "UNION " +
                "SELECT user_username " +
                "FROM public.friends " +
                "WHERE friend_username= ? and is_friend = true";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, username);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendUsername = resultSet.getString("friend_username");

                    friends.add(new Friend(friendUsername, true));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return friends;
    }

    public Collection<Friend> getListFriendOnline(String username) {
        Collection<Friend> friends = new ArrayList<>();

        String sql = "SELECT friend.friend_username " +
                "FROM " +
                "(SELECT friend_username " +
                "FROM public.friends WHERE user_username = ? and is_friend = true " +
                "UNION " +
                "SELECT user_username FROM public.friends WHERE friend_username= ? and is_friend = true) as friend " +
                "join public.users on friend.friend_username = users.username " +
                "WHERE is_online = true";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, username);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendUsername = resultSet.getString("friend_username");

                    friends.add(new Friend(friendUsername, true));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return friends;
    }

    public Collection<Friend> getListRequestFriend(String username) {
        Collection<Friend> friends = new ArrayList<>();

        String sql = "SELECT user_username " +
                "FROM public.friends " +
                "WHERE friend_username = ? and is_friend = false";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendUsername = resultSet.getString("user_username");

                    friends.add(new Friend(friendUsername, false));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return friends;
    }

    public Optional<Boolean> unfriend(String user, String friend) {
        String sql = "DELETE FROM public.friends WHERE user_username = ? AND friend_username = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, user);
                statement.setString(2, friend);

                int numberRowDelete = statement.executeUpdate();

                if (numberRowDelete > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<Boolean> addFriend(String user, String friend) {
        String sql = "INSERT INTO public.friends (user_username, friend_username, created_at, is_friend) " +
                "VALUES (?, ?, now(), false)";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, user);
                statement.setString(2, friend);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<Boolean> acceptFriend(String user, String friend) {
        String sql = "UPDATE public.friends " +
                "SET is_friend = true " +
                "WHERE user_username = ? and friend_username = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, friend);
                statement.setString(2, user);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<Boolean> rejectFriend(String user, String friend) {
        String sql = "DELETE FROM public.friends WHERE user_username = ? and friend_username = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, friend);
                statement.setString(2, user);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }
}
