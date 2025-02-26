package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utilit.GetDomain;
import hexlet.code.utilit.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class TestJavalin {

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
    public void testGetDomain() throws SQLException {
        Url url = new Url(GetDomain.get("https://codeclimate.com/github/Sanapol/java-project-72"));
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPage(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com").doesNotContain("github");
        });
    }

    @Test
    public void testEntities() throws SQLException {
        Url url1 = new Url("https://codeclimate.com/github/Sanapol/java-project-72");
        Url url2 = new Url("https://htmlbook.ru/samhtml/tekst/spetssimvoly");
        UrlRepository.save(url1);
        UrlRepository.save(url2);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPage());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com")
                    .contains("https://htmlbook.ru");
        });
    }
}
