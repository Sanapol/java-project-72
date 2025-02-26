@import hexlet.code.model.Url
@import hexlet.code.utilit.NamedRoutes
@import hexlet.code.websites.UrlsPage

@param UrlsPage page

@template.page(page = page,
    content = @`
    <div class="col">
        <table class="table">
            <thead>
                <tr>
                    <th>id</th>
                    <th>сайт</th>
                    <th>дата проверки</th>
                </tr>
            </thead>
            <tbody>
                @for(Url url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="${NamedRoutes.urlPage(url.getId())}">${url.getName()}</a></td>
                        <td>${String.valueOf(url.getCreatedAt())}</td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
`)
