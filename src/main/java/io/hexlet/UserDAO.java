package io.hexlet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            var queryInsert = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();

                var generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong("id"));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            var queryUpdate = "UPDATE users SET phone = ? WHERE username = ?";
            try (var preparedStatement = connection.prepareStatement(queryUpdate)) {
                preparedStatement.setString(1, user.getPhone());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.executeUpdate();
            }
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        var querySelect = "SELECT * FROM users WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(querySelect)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var user = new User (username, phone);

                user.setId(id);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        }
    }

    public void delete(Long id) throws SQLException {
        var queryDelete = "DELETE from users WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(queryDelete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }
    public List<User> getEntities() throws SQLException {
        var users = new ArrayList<User>();
        var querySelectAll = "SELECT * FROM users";
        try (var stmt = connection.createStatement()) {
            var resultSet = stmt.executeQuery(querySelectAll);
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var user = new User(name, phone);
                user.setId(id);
                users.add(user);
            }
        }
        return users;
    }
}