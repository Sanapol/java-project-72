@import hexlet.code.model.Website
@import hexlet.code.websites.WebsitesPage

@param WebsitesPage page

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
                @for(Website website : page.getWebsites())
                    <tr>
                        <td>${website.getId()}</td>
                        <td>${website.getName()}</td>
                        <td>${String.valueOf(website.getCreated_at())}</td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
`)
