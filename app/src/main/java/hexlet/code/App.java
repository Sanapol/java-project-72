package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controllers.RootController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;
import hexlet.code.repository.BaseRepository;
import hexlet.code.utilit.NamedRoutes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        String port = System.getenv().getOrDefault("PORT", "7070");
        app.start(Integer.parseInt(port));
    }

    public static Javalin getApp() throws IOException, SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:project");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        InputStream url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        String sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));

        log.info(sql);
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get(NamedRoutes.mainPage(), RootController::index);

        return app;
    }
}
