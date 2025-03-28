package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utilit.GetDomain;
import hexlet.code.utilit.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import io.micrometer.core.instrument.util.IOUtils;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.h2.util.json.JSONArray;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
    public final void getUp() throws SQLException {
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
            String requestBody = "name=https://www.example.com";
            Response response = client.post("/urls", requestBody);
            Optional<Url> request = UrlRepository.findByName(requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertThat(request).isNotNull();
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
    public void testFindByName() throws SQLException, MalformedURLException {
        String name = "https://codeclimate.com/github/Sanapol/java-project-72";
        Url url = new Url(GetDomain.get(name));
        UrlRepository.save(url);
        Optional<Url> entity = UrlRepository.findByName(GetDomain.get(name));
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPage(entity.get().getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://codeclimate.com").doesNotContain("github");
        });
    }

    @Test
    public void testChecks() throws SQLException {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        InputStream stream = JSONArray.class.getClassLoader().getResourceAsStream("htmlForTest.html");
        String jsonBody = IOUtils.toString(stream);
        MockResponse mockResponse = new MockResponse()
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(jsonBody);
        mockWebServer.enqueue(mockResponse);
        Url website = new Url(baseUrl);
        UrlRepository.save(website);

        JavalinTest.test(app, (server, client) -> {
            Response response = client.post(NamedRoutes.urlChecks(website.getId()));
            assertThat(response.body().string()).contains("hello i am title")
                    .contains("hello i am h1").contains("hello i am description");
        });
    }

    @Test
    public void testEntities() throws SQLException, MalformedURLException {
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

    @Test
    public void testPost() {
        JavalinTest.test(app, (server, client) -> {
            var name = "http://localhost:50275";
            var requestBody = "url=" + name;
            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

            var actualUrl = UrlRepository.findByName(name);
            assertThat(actualUrl).isNotNull();
        });
    }


    @AfterAll
    static void serverOff() throws IOException {
        mockWebServer.shutdown();
    }
}
