package hexlet.code.repository;

import hexlet.code.model.Website;
import org.h2.expression.function.CurrentDateTimeValueFunction;

import javax.annotation.processing.Generated;
import javax.swing.text.html.Option;
import java.lang.reflect.Parameter;
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

public class WebsiteRepository extends BaseRepository{

    public static void save(Website website) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, website.getName());
            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                website.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("BD have not returned id key");
            }
        }
    }

    public static List<Website> getEntities() throws SQLException{
        String sql = "SELECT * FROM urls";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
            List<Website> result = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Website website = new Website(name);
                website.setId(id);
                website.setCreated_at(created_at);
                result.add(website);
            }
            return result;
        }
    }

    public static Optional<Website> findByName(Website website) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, website.getName());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Website result = new Website(name);
                website.setId(id);
                website.setCreated_at(created_at);
                return Optional.of(result);
            }
            return Optional.empty();
        }
    }

    public static Optional<Website> find(long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Website website = new Website(name);
                website.setId(id);
                website.setCreated_at(created_at);
                return Optional.of(website);
            }
            return Optional.empty();
        }
    }
}
