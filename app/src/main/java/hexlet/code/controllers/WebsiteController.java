package hexlet.code.controllers;

import hexlet.code.model.Website;
import hexlet.code.repository.WebsiteRepository;
import hexlet.code.utilit.GetDomain;
import hexlet.code.utilit.NamedRoutes;
import hexlet.code.websites.BuildWebsitePage;
import hexlet.code.websites.WebsitePage;
import hexlet.code.websites.WebsitesPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;

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
            Website website = new Website(GetDomain.get(name));
            Optional<Website> repeat = WebsiteRepository.findByName(website);
            if (repeat.isEmpty()) {
                WebsiteRepository.save(website);
                ctx.sessionAttribute("flash", "Успешно");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect(NamedRoutes.urlsPage());
            } else {
                ctx.sessionAttribute("flash", "Сайт уже существует");
                ctx.sessionAttribute("flash-type", "warning");
                ctx.redirect(NamedRoutes.urlPage(website.getId()));
            }
        } catch (ValidationException e) {
            String name = ctx.formParam("name");
            BuildWebsitePage page = new BuildWebsitePage(name, e.getErrors());
            ctx.render("pages/index.jte", model("page", page));
        }
    }

    public static void urls(Context ctx) throws SQLException {
        List<Website> websites = WebsiteRepository.getEntities();
        WebsitesPage page = new WebsitesPage(websites);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("pages/urls.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Website website = WebsiteRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("site not found"));
        WebsitePage page = new WebsitePage(website);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("pages/url.jte", model("page", page));
    }
}
