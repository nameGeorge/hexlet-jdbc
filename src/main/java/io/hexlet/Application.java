package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {
        // Создаем соединение с базой в памяти
        // База создается прямо во время выполнения этой строчки
        // Здесь hexlet_test — это имя базы данных
        try( var connection = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var query = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255));";
            try (var statement = connection.createStatement()) {
                statement.execute(query);
            }
            var query2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(query2)) {
                preparedStatement.setString(1, "tommy");
                preparedStatement.setString(2, "123456789");
                preparedStatement.executeUpdate();
                preparedStatement.setString(1, "timmy");
                preparedStatement.setString(2, "1222222");
                preparedStatement.executeUpdate();
                preparedStatement.setString(1, "tammy");
                preparedStatement.setString(2, "123333333");
                preparedStatement.executeUpdate();
            }
            var query3 = "SELECT * FROM users";
            try (var statement = connection.createStatement()) {
                var resultSet = statement.executeQuery(query3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getLong("id"));
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
            System.out.println("\n\n");
            var queryDeleteUser = "DELETE FROM users WHERE phone = ?";
            try (var preparedStatement = connection.prepareStatement(queryDeleteUser)) {
                preparedStatement.setString(1,"123333333");
                preparedStatement.executeUpdate();
            }
            var query4 = "SELECT * FROM users";
            try (var statement = connection.createStatement()) {
                var resultSet = statement.executeQuery(query4);
                while (resultSet.next()) {
                    System.out.println(resultSet.getLong("id"));
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
            //Возврат id нового объекта внесенного в базу
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Sarah");
                preparedStatement.setString(2, "333333333");
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
                preparedStatement.setString(1, "Peter");
                preparedStatement.setString(2, "3234234523");
                preparedStatement.executeUpdate();
                // Если ключ составной, значений может быть несколько
                // В нашем случае, ключ всего один
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }
        }
    }
}