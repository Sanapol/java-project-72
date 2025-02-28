package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utilit.GetDomain;
import hexlet.code.utilit.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class TestApp {

    private Javalin app;
    public static MockWebServer mockWebServer;

    @BeforeAll
    static void serverStart() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

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
    public void testFindByName() throws SQLException {
        Url url = new Url(GetDomain.get("https://codeclimate.com/github/Sanapol/java-project-72"));
        UrlRepository.save(url);
        Optional<Url> entity = UrlRepository.findByName(url);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPage(entity.get().getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com").doesNotContain("github");
        });
    }

    @Test
    public void testChecks() throws SQLException {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        MockResponse mockResponse = new MockResponse()
                .setHeader("Content-Type", "application/json; charset=utf-8");
        mockResponse.setBody("<title>hello i am title</title> "
                + "<h1>hello i am h1</h1> <meta name=\"description\" content=\"hello i am description\">");
        mockWebServer.enqueue(mockResponse);
        HttpUrl url = mockWebServer.url(baseUrl);
        Url website = new Url(baseUrl);
        UrlRepository.save(website);

        JavalinTest.test(app, (server, client) -> {
            Response response = client.post(NamedRoutes.urlChecks(1));
            assertThat(response.body().string()).contains("hello i am title")
                    .contains("hello i am h1").contains("hello i am description");
        });
    }

    @Test
    public void testEntities() throws SQLException {
        Url url1 = new Url(GetDomain.get("https://codeclimate.com/github/Sanapol/java-project-72"));
        Url url2 = new Url(GetDomain.get("https://htmlbook.ru/samhtml/tekst/spetssimvoly"));
        UrlRepository.save(url1);
        UrlRepository.save(url2);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPage());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com")
                    .contains("https://htmlbook.ru");
        });
    }

    @AfterAll
    static void serverOff() throws IOException {
        mockWebServer.shutdown();
    }
}
