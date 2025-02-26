package hexlet.code.controllers;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utilit.GetDomain;
import hexlet.code.utilit.NamedRoutes;
import hexlet.code.websites.BuildWebsitePage;
import hexlet.code.websites.ChecksPage;
import hexlet.code.websites.UrlPage;
import hexlet.code.websites.UrlsPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

public class WebsiteController {

    public static void index(Context ctx) {
        BuildWebsitePage page = new BuildWebsitePage();
        ctx.render("pages/index.jte", model("page", page));
    }

    public static void buildUrls(Context ctx) throws SQLException {
        try {
            String name = ctx.formParamAsClass("name", String.class)
                    .check(n -> !n.isEmpty(), "Поле не должно быть пустым")
                    .check(n -> n.contains("//"), "Некорректный URL")
                    .get();
            Url url = new Url(GetDomain.get(GetDomain.get(name)));
            Optional<Url> repeat = UrlRepository.findByName(url);
            if (repeat.isEmpty()) {
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Успешно");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect(NamedRoutes.urlsPage());
            } else {
                ctx.sessionAttribute("flash", "Сайт уже существует");
                ctx.sessionAttribute("flash-type", "warning");
                ctx.redirect(NamedRoutes.urlPage(url.getId()));
            }
        } catch (ValidationException e) {
            String name = ctx.formParam("name");
            BuildWebsitePage page = new BuildWebsitePage(name, e.getErrors());
            ctx.render("pages/index.jte", model("page", page));
        }
    }

    public static void urls(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        UrlsPage page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("pages/urls.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("site not found"));
        UrlPage page = new UrlPage(url);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ChecksPage checksPage = UrlCheckRepository.getCheckList(id);
        ctx.render("pages/url.jte", model("page", page, "checks", checksPage));
    }

    public static void check(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Url name = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("current id " + id + " not found"));

        try {
            HttpResponse<String> response = Unirest.get(name.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());
            int statusCode = response.getStatus();
            String title = doc.title();
            String h1 = doc.select("h1").text();
            String description = doc.select("meta[name=description]").attr("content");
            UrlCheck urlCheck = new UrlCheck(id, statusCode, title, h1, description);
            UrlCheckRepository.check(urlCheck);
            ctx.redirect(NamedRoutes.urlPage(id));
        } catch (SQLException e) {
            throw new SQLException("check is failed");
        }
    }
}
