@import hexlet.code.utilit.NamedRoutes
@import hexlet.code.websites.BuildWebsitePage
@import io.javalin.validation.ValidationError
@import java.util.List

@param BuildWebsitePage page

@template.page(page = page,
    content = @`
    <section class="flex-grow-6 container-fluid bg-dark p-5">
    <div class="col-md-10 col-lg-8 mx-auto text-white">
        <form action="${NamedRoutes.urlsPage()}" method="post">
            <p class="lead">Проверка сайта на SEO пригодность</p>
            <div class="row">
                <label class="col">
                    <input class="form-control w-100 form-floating" type="url" name="name" value="${page.getName()}" placeholder="ссылка">
                </label>
                <input type="submit" value="принять" class="col-auto btn btn-primary">
                <p class="mt-2 mb-0 text-white">Пример: https://www.example.com</p>
            </div>
        </form>
    </div>
    </section>
    <section class="flex-grow-1 container-fluid bg-white p-5"></section>
`)
