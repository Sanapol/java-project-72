@import hexlet.code.model.UrlCheck
@import hexlet.code.utilit.NamedRoutes
@import hexlet.code.websites.ChecksPage
@import hexlet.code.websites.UrlPage
@import hexlet.code.websites.ChecksPage

@param UrlPage page
@param ChecksPage checks

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
                    <tr>
                        <td>${page.getUrl().getId()}</td>
                        <td><a href="${page.getUrl().getName()}">${page.getUrl().getName()}</a></td>
                        <td>${String.valueOf(page.getUrl().getCreatedAt())}</td>
                    </tr>
                </tbody>
            </table>
            <div>
                <form action="${NamedRoutes.urlChecks(page.getUrl().getId())}" method="post">
                    <input type="submit" value="обновить статус" class="btn btn-primary">
                </form>
            </div>
            <div class="col">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Код ответа</th>
                            <th>title</th>
                            <th>h1</th>
                            <th>description</th>
                            <th>Дата проверки</th>
                        </tr>
                    </thead>
                    <tbody>
                        @if(checks != null && checks.getChecks() != null)
                            @for(UrlCheck check : checks.getChecks())
                                <tr>
                                    <td>${check.getId()}</td>
                                    <td>${check.getStatusCode()}</td>
                                    <td>${check.getTitle()}</td>
                                    <td>${check.getH1()}</td>
                                    <td>${check.getDescription()}</td>
                                    <td>${String.valueOf(check.getCreatedAt())}</td>
                                </tr>
                            @endfor
                        @endif
                    </tbody>
                </table>
            </div>
        </div>
`)
