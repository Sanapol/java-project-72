package hexlet.code;

import hexlet.code.model.Website;
import hexlet.code.repository.WebsiteRepository;
import hexlet.code.utilit.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class AppTest {

    private Javalin app;

    @BeforeEach
    public final void getUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            String requestBody = "name=test";
            Response response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("test");
        });
    }

    @Test
    public void testNotFound() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPage(111));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testRepositorySave() throws SQLException {
        Website website = new Website("https://codeclimate.com/github/Sanapol/java-project-72");
        WebsiteRepository.save(website);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPage(website.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com").doesNotContain("github");
        });
    }

    @Test
    public void testEntities() throws SQLException {
        Website website1 = new Website("https://codeclimate.com/github/Sanapol/java-project-72");
        Website website2 = new Website("https://htmlbook.ru/samhtml/tekst/spetssimvoly");
        WebsiteRepository.save(website1);
        WebsiteRepository.save(website2);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPage());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com")
                    .contains("https://htmlbook.ru");
        });
    }
}
