package org.nmjava.chatapp.commons.daos;

import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.nmjava.chatapp.commons.models.User;
import org.nmjava.chatapp.commons.utils.ConnectDB;

import java.security.SecureRandom;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class UserDao {
    private final Optional<Connection> connection;

    public UserDao() {
        connection = ConnectDB.getConnection();
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private boolean checkPassword(String plaintext, String hashPw) {
        return BCrypt.checkpw(plaintext, hashPw);
    }

    private String generatedPassword(int length) {
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()-_=+[{]}|;:,.?").toCharArray();
        return RandomStringUtils.random(length, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom());
    }

    public Optional<Boolean> isUserExists(String username) {
        String sql = "SELECT exists(SELECT 1 FROM public.users WHERE username = ?)";

        return connection.flatMap(conn -> {
            Optional<Boolean> isExists = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getBoolean(1))
                        isExists = Optional.of(true);
                }


            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return isExists;
        });
    }

    public Optional<Boolean> isAdmin(String username) {
        String sql = "SELECT exists(select 1 from public.admin_user WHERE username = ?)";

        return connection.flatMap(conn -> {
            Optional<Boolean> isAdmin = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    if (resultSet.getBoolean(1))
                        isAdmin = Optional.of(true);
                }

                resultSet.close();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return isAdmin;
        });
    }

    public Optional<Boolean> isAuthUser(String username, String plainPassword) {
        String getPassword = "SELECT password " +
                             "FROM public.users " +
                             "WHERE username = ? AND is_activated = true";

        String logUserLogin = "INSERT INTO public.login_logs (username, login_at) VALUES (?, now())";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(getPassword)) {
                statement.setString(1, username);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String hashPassword = resultSet.getString("password");
                    if (checkPassword(plainPassword, hashPassword)) {
                        PreparedStatement logUserLoginStatement = conn.prepareStatement(logUserLogin);
                        logUserLoginStatement.setString(1, username);

                        int rowInsert = logUserLoginStatement.executeUpdate();

                        if (rowInsert > 0)
                            isSuccess = Optional.of(true);

                        logUserLoginStatement.close();
                    }

                }

                resultSet.close();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<String> getUsernameByEmail(String email) {
        String sql = "SELECT username from public.users WHERE email = ?";

        return connection.flatMap(conn -> {
            Optional<String> username = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, email);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    username = Optional.of(resultSet.getString(1));
                }


            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return username;
        });
    }

    public Optional<String> resetPassword(String email) {
        String newPassword = generatedPassword(6);
        System.out.println("NewPassword ###" + newPassword);
        String hashPassword = hashPassword(newPassword);

        String sql = "UPDATE public.users SET password = ? WHERE email = ?";

        return connection.flatMap(conn -> {
            Optional<String> rtnPassword = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, hashPassword);
                statement.setString(2, email);

                int rowOfUpdate = statement.executeUpdate();

                if (rowOfUpdate > 0) {
                    rtnPassword = Optional.of(newPassword);
                }

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return rtnPassword;
        });
    }

    public Collection<User> getInfoAll() {
        Collection<User> users = new ArrayList<>();
        String sql = "SELECT username, full_name, address, date_of_birth, gender, email, is_online, is_activated " +
                     "FROM public.users";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String fullName = resultSet.getString("full_name");
                    String address = resultSet.getString("address");
                    LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                    String gender = resultSet.getString("gender");
                    String email = resultSet.getString("email");
                    Boolean online = resultSet.getBoolean("is_online");
                    Boolean active = resultSet.getBoolean("is_activated");

                    User user = new User(username, fullName, address, dateOfBirth, gender, email, online, active);
                    user.setUsername(resultSet.getString("username"));

                    users.add(user);
                }
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });

        return users;
    }

    public Optional<String> save(User user) {
        User nonNullUser = Objects.requireNonNull(user, "The user is null");

        String sql = "INSERT INTO public.users (username, password, full_name, address, date_of_birth, gender, email, is_online, is_activated, create_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "RETURNING username";

        return connection.flatMap(conn -> {
            Optional<String> id = Optional.empty();
            System.out.println("Password Create new Account###" + nonNullUser.getPassword());
            try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nonNullUser.getUsername());
                statement.setString(2, this.hashPassword(nonNullUser.getPassword()));
                statement.setString(3, nonNullUser.getFullName());
                statement.setString(4, nonNullUser.getAddress());
                statement.setDate(5, Date.valueOf(nonNullUser.getDateOfBirth()));
                statement.setString(6, nonNullUser.getGender());
                statement.setString(7, nonNullUser.getEmail());
                statement.setBoolean(8, nonNullUser.getOnline());
                statement.setBoolean(9, nonNullUser.getActivated());
                statement.setTimestamp(10, Timestamp.valueOf(nonNullUser.getCreateAt()));

                int numberOfInsertRows = statement.executeUpdate();

                if (numberOfInsertRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            id = Optional.of(resultSet.getString(1));
                        }
                    }
                }

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return id;
        });
    }

    public Optional<Boolean> updateUserOnline(String username, Boolean isUserOnline) {
        String sql = "UPDATE public.users SET is_online = ? WHERE username = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setBoolean(1, isUserOnline);
                statement.setString(2, username);

                int numberOfUpdateRows = statement.executeUpdate();

                if (numberOfUpdateRows > 0)
                    isSuccess = Optional.of(true);

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public void update(User user) {
        String sql = "UPDATE public.users " +
                     "SET full_name = ?, address = ?, date_of_birth = ?, gender = ?, email = ?, is_online = ?, is_activated = ? " +
                     "WHERE username = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, user.getFullName());
                statement.setString(2, user.getAddress());
                statement.setDate(3, Date.valueOf(user.getDateOfBirth()));
                statement.setString(4, user.getGender());
                statement.setString(5, user.getEmail());
                statement.setBoolean(6, user.getOnline());
                statement.setBoolean(7, user.getActivated());
                statement.setString(8, user.getUsername());

                int numberOfDeletedRows = statement.executeUpdate();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });
    }

    public void resetPassword(User user) {
        String sql = "UPDATE public.users " +
                "SET password = ? " +
                "WHERE username = ?";

        connection.ifPresent(conn -> {
            String hashPassword = hashPassword(user.getPassword());
            ;
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, hashPassword);
                statement.setString(2, user.getUsername());

                int numberOfDeletedRows = statement.executeUpdate();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });
    }

    public void delete(String username) {
        String sql = "DELETE FROM public.users WHERE username = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);

                int numberOfDeletedRows = statement.executeUpdate();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });
    }
}
