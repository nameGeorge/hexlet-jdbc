package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:h2:mem:hexlet-test")) {
            var query = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255));";
            try (var statement = connection.createStatement()) {
                statement.execute(query);
            }
            var dao = new UserDAO(connection);

            var users = new ArrayList<User>(List.of(
                    new User("Vitya", "235456"),
                    new User("Masha", "3456345"),
                    new User("Alina", "62455624556"),
                    new User("Nikolay", "342523455"),
                    new User("Oleg", "112452345")));

            for (User user : users) {
                dao.save(user);
                System.out.println(user.getId());
            }

            System.out.println(dao.find(3L).get());
            dao.delete(3L);
            if (dao.find(3L).isPresent())
            {
                System.out.println(dao.find(3L).get());
            } else {
                System.out.println("Not found");
            }
            System.out.println(dao.getEntities());
        }
    }
}