@import hexlet.code.websites.WebsitePage

@param WebsitePage page

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
                        <td>${page.getWebsite().getId()}</td>
                        <td><a href="${page.getWebsite().getName()}">${page.getWebsite().getName()}</a></td>
                        <td>${String.valueOf(page.getWebsite().getCreatedAt())}</td>
                    </tr>
                </tbody>
            </table>
        </div>
`)
