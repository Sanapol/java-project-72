package hexlet.code;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWebServer {
    public static MockWebServer mockWebServer;

    @BeforeAll
    static void serverStart() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    public void testCheck() throws IOException, InterruptedException {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        MockResponse mockResponse = new MockResponse()
                .setHeader("Content-Type", "application/json; charset=utf-8");
        mockResponse.setBody("<title>hello i am title</title> "
                + "<h1>hello i am h1</h1> <meta name=\"description\" content=\"hello i am description\">");
        mockWebServer.enqueue(mockResponse);
        HttpUrl url = mockWebServer.url(baseUrl);

        HttpResponse<String> response = Unirest.get(String.valueOf(url)).asString();
        Document doc = Jsoup.parse(response.getBody());
        String title = doc.title();
        String h1 = doc.select("h1").text();
        String description = doc.select("meta[name=description]").attr("content");;

        assertThat(title).contains("hello i am title");
        assertThat(h1).contains("hello i am h1");
        assertThat(description).contains("hello i am description");
    }

    @AfterAll
    static void serverOff() throws IOException {
        mockWebServer.shutdown();
    }
}
