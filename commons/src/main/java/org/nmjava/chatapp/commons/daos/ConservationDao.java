package org.nmjava.chatapp.commons.daos;

import org.nmjava.chatapp.commons.models.Conservation;
import org.nmjava.chatapp.commons.models.Message;
import org.nmjava.chatapp.commons.utils.ConnectDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ConservationDao {

    private final Optional<Connection> connection;

    public ConservationDao() {
        connection = ConnectDB.getConnection();
    }

    private UUID genID() {
        return UUID.randomUUID();
    }

    public Collection<Conservation> getListConservation(String username) {
        Collection<Conservation> conservations = new ArrayList<>();

        String sql = "SELECT con.conservation_id, con.name, conupdate.last_message ,con.is_group " + "FROM public.conservation_user conuser " + "join public.conservations con on con.conservation_id = conuser.conservation_id " + "join public.conservation_update conupdate on conupdate.conservation_id = con.conservation_id " + "WHERE username = ? " + "ORDER BY conupdate.last_update";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String conservationID = resultSet.getString("conservation_id");
                    String name = resultSet.getString("name");
                    String last_message = resultSet.getString("last_message");
                    Boolean is_Group = resultSet.getBoolean("is_group");

                    conservations.add(new Conservation(conservationID, name, last_message, is_Group));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return conservations;
    }

//    public Optional<Conservation> getInfoConservation(String conservationID) {
//
//    }

    public Collection<Message> getListMessageConservation(String conservationId, String username) {
        Collection<Message> messages = new ArrayList<>();

        String sql =
                "SELECT mes.message_id, mes.sender_username, mes.create_at, mes.message " +
                        "FROM public.messages mes " +
                        "JOIN public.conservations con ON mes.conservation_id = con.conservation_id " +
                        "WHERE mes.conservation_id = ? " +
                        "AND mes.message_id NOT IN (SELECT message_id " +
                        "FROM public.message_hide " +
                        "WHERE conservation_id = ? AND username = ?)" +
                        "ORDER BY mes.create_at DESC";

        connection.ifPresent(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationId);
                statement.setString(2, conservationId);
                statement.setString(3, username);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String messageID = resultSet.getString("message_id");
                    String sender = resultSet.getString("sender_username");
                    String message = resultSet.getString("message");
                    String create_at = resultSet.getString("create_at");

                    messages.add(new Message(messageID, sender, conservationId, create_at, message));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

        });

        return messages;
    }

    public Optional<Boolean> createConservation(String creatorName, List<String> users, Boolean isGroup) {
        String createConservationSQL = "INSERT INTO public.conservations (conservation_id, creator_username, name, created_at, is_group) " + "VALUES (?, ?, ?, ?, ?) " + "RETURNING conservation_id";
        String addUserToConservationSQL = "INSERT INTO public.conservation_user (conservation_id, username, role) " + "VALUES(?, ?, ?) " + "RETURNING conservation_id";
        String addUpdateToConservationSQL = "INSERT INTO public.conservation_update (conservation_id, last_message, last_sender, last_update) " + "VALUES(?,null,null,now()) " + "RETURNING conservation_id";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try {
                Optional<String> conservationID = Optional.empty();

                PreparedStatement createConservationStatement = conn.prepareStatement(createConservationSQL, Statement.RETURN_GENERATED_KEYS);
                createConservationStatement.setString(1, genID().toString());
                createConservationStatement.setString(2, creatorName);
                if (users.size() > 1) {
                    createConservationStatement.setString(3, new String(creatorName + " " + users.get(0) + " " + users.get(1)));
                } else {
                    createConservationStatement.setString(3, new String(creatorName + " " + users.get(0)));
                }
                createConservationStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                createConservationStatement.setBoolean(5, isGroup);
                int rowOfUpdate = createConservationStatement.executeUpdate();
                if (rowOfUpdate > 0) {
                    try (ResultSet resultSet = createConservationStatement.getGeneratedKeys()) {
                        if (resultSet.next()) conservationID = Optional.of(resultSet.getString(1));
                    }

                }

                if (conservationID.isPresent()) {
                    PreparedStatement addUserToConservationStatement = conn.prepareStatement(addUserToConservationSQL, Statement.RETURN_GENERATED_KEYS);
                    addUserToConservationStatement.setString(1, conservationID.get());
                    addUserToConservationStatement.setString(2, creatorName);
                    addUserToConservationStatement.setInt(3, 0);

                    rowOfUpdate = addUserToConservationStatement.executeUpdate();
                    if (rowOfUpdate < 1) return isSuccess;

                    if (!isGroup) {
                        addUserToConservationStatement.setString(1, conservationID.get());
                        addUserToConservationStatement.setString(2, users.get(0));
                        addUserToConservationStatement.setInt(3, 0);

                        rowOfUpdate = addUserToConservationStatement.executeUpdate();
                        if (rowOfUpdate < 1) return isSuccess;
                    } else {
                        for (String user : users) {
                            addUserToConservationStatement.setString(1, conservationID.get());
                            addUserToConservationStatement.setString(2, user);
                            addUserToConservationStatement.setInt(3, 2);

                            rowOfUpdate = addUserToConservationStatement.executeUpdate();
                            if (rowOfUpdate < 1) return isSuccess;
                        }
                    }
                    PreparedStatement addUpdateToConservationStatement = conn.prepareStatement(addUpdateToConservationSQL, Statement.RETURN_GENERATED_KEYS);
                    addUpdateToConservationStatement.setString(1, conservationID.get());
                    rowOfUpdate = addUpdateToConservationStatement.executeUpdate();
                    if (rowOfUpdate < 1) return isSuccess;
                }


            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Collection<String> getMember(String conservationID) {
        Collection<String> users = new ArrayList<>();

        String sql = "SELECT username " + "FROM public.conservation_user " + "WHERE conservation_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    users.add(resultSet.getString(1));
                }
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });

        return users;
    }

    public Collection<String> getMemberExceptRequester(String conservationID, String requester) {
        Collection<String> users = new ArrayList<>();

        String sql = "SELECT username " + "FROM public.conservation_user " + "WHERE conservation_id = ? and username != ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, requester);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    users.add(resultSet.getString(1));
                }
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        });

        return users;
    }

    public Collection<String> sentMessage(String conservationID, String username, String message) {
        Collection<String> users = new ArrayList<>();

        String insertMessage = "INSERT INTO public.messages (message_id, sender_username, conservation_id, create_at, message)  " + "VALUES (?, ?, ?, ?, ?)";

        String updateConservation = "UPDATE public.conservation_update " + "SET last_message = ?, last_sender = ?, last_update = ? " + "WHERE conservation_id = ?";

        String getMemberInConservation = "SELECT username " + "FROM public.conservation_user " + "WHERE conservation_id = ? and username != ?";

        connection.ifPresent(conn -> {
            try {
                LocalDateTime createAt = LocalDateTime.now();

                PreparedStatement insertMessageStatement = conn.prepareStatement(insertMessage);
                insertMessageStatement.setString(1, genID().toString());
                insertMessageStatement.setString(2, username);
                insertMessageStatement.setString(3, conservationID);
                insertMessageStatement.setTimestamp(4, Timestamp.valueOf(createAt));
                insertMessageStatement.setString(5, message);

                int numberOfRow = insertMessageStatement.executeUpdate();

                PreparedStatement updateConservationStatement = conn.prepareStatement(updateConservation);
                updateConservationStatement.setString(1, message);
                updateConservationStatement.setString(2, username);
                updateConservationStatement.setTimestamp(3, Timestamp.valueOf(createAt));
                updateConservationStatement.setString(4, conservationID);

                numberOfRow = updateConservationStatement.executeUpdate();

                PreparedStatement getMemberInConservationStatement = conn.prepareStatement(getMemberInConservation);
                getMemberInConservationStatement.setString(1, conservationID);
                getMemberInConservationStatement.setString(2, username);

                ResultSet resultSet = getMemberInConservationStatement.executeQuery();
                while (resultSet.next()) {
                    users.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });


        return users;
    }

    public Optional<Boolean> deleteMessage(String conservationID, String username, String messageID) {
        String sql = "INSERT INTO public.message_hide (conservation_id, username, message_id) " + "VALUES (?, ?, ?)";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, username);
                statement.setString(3, messageID);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }
    public Optional<Boolean> isConservationBefore(String first, String second) {
        String sql = "SELECT fn_isConservationBefore(?, ?)";

        return connection.flatMap(conn -> {
            Optional<Boolean> isExists = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, first);
                statement.setString(2, second);

                System.out.println(statement);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    if (resultSet.getBoolean(1))
                        isExists = Optional.of(true);
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isExists;
        });
    }
    public Collection<Message> searchMessageInConservation(String conservationID, String text) {
        Collection<Message> messages = new ArrayList<>();

        String sql = "SELECT message_id, sender_username, conservation_id, create_at, message FROM messages WHERE conservation_id = ? AND LOWER(message) LIKE ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, "%" + text.toLowerCase() + "%");

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String messageID = resultSet.getString("message_id");
                    String sender = resultSet.getString("sender_username");
                    String create_at = resultSet.getString("create_at");
                    String message = resultSet.getString("message");

                    messages.add(new Message(messageID, sender, conservationID, create_at, message));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return messages;
    }

    public Collection<Message> searchAllMessage(String text) {
        Collection<Message> messages = new ArrayList<>();

        String sql = "SELECT message_id, sender_username, conservation_id, create_at, message FROM messages WHERE LOWER(message) LIKE ?";

        connection.ifPresent(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, "%" + text.toLowerCase() + "%");

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String messageID = resultSet.getString("message_id");
                    String sender = resultSet.getString("sender_username");
                    String conservationID = resultSet.getString("conservation_id");
                    String create_at = resultSet.getString("create_at");
                    String message = resultSet.getString("message");

                    messages.add(new Message(messageID, sender, conservationID, create_at, message));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return messages;
    }

    public Optional<Boolean> renameConservation(String conservationID, String newName) {
        String sql = "UPDATE public.conservations " + "SET name = ? " + "WHERE conservation_id = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, newName);
                statement.setString(2, conservationID);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<Integer> getRoleUserConservation(String conservationID, String user) {
        String sql = "SELECT role " + "FROM public.conservation_user " + "WHERE conservation_id = ? and username = ?";

        return connection.flatMap(conn -> {
            Optional<Integer> role = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, user);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    role = Optional.of(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return role;
        });
    }

    public Optional<Boolean> addMemberGroupChat(String conservationID, String member) {
        String sql = "INSERT INTO public.conservation_user (conservation_id, username, role) " + "VALUES (?, ?, 2)";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, member);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<Boolean> giveAdminUserGroupChat(String conservationID, String member) {
        String sql = "UPDATE public.conservation_user " + "SET role = 1 " + "WHERE conservation_id = ? and username = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, member);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }

    public Optional<Boolean> removeUserGroupChat(String conservationID, String member) {
        String sql = "DELETE FROM public.conservation_user " + "WHERE conservation_id = ? and username = ?";

        return connection.flatMap(conn -> {
            Optional<Boolean> isSuccess = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, conservationID);
                statement.setString(2, member);

                int numberOfRowUpdate = statement.executeUpdate();

                if (numberOfRowUpdate > 0) isSuccess = Optional.of(true);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

            return isSuccess;
        });
    }


    public Collection<Conservation> getListGroup() {
        Collection<Conservation> conservations = new ArrayList<>();

        String sql = "select cer.conservation_id, con.name, users.full_name, cer.role " +
                "from conservation_user cer " +
                "join conservations con on cer.conservation_id = con.conservation_id " +
                "join users on cer.username = users.username " +
                "where con.is_group = true";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String conservationID = resultSet.getString("conservation_id");
                    String grname = resultSet.getString("name");
                    String full_name = resultSet.getString("full_name");
                    Integer role = resultSet.getInt("role");

                    conservations.add(new Conservation(conservationID, grname, full_name, role));
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        });

        return conservations;
    }

}


