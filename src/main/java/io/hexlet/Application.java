package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;

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
            var query2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
            try (var statement2 = connection.createStatement()) {
                statement2.executeUpdate(query2);
            }
            var query3 = "SELECT * FROM users";
            try (var statement3 = connection.createStatement()) {
                var resultSet = statement3.executeQuery(query3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getLong("id"));
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}