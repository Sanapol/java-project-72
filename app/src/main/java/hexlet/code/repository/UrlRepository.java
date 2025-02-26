package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("BD have not returned id key");
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = "SELECT * FROM urls";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
            List<Url> result = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                result.add(url);
            }
            return result;
        }
    }

    public static Optional<Url> findByName(Url url) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url.getName());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Url result = new Url(url.getName());
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(result);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> find(long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }
}
