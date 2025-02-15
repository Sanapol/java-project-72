@import hexlet.code.websites.WebsitePage

@param WebsitePage page

@template.page(page = page,
    content = @`
    <div>
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
                        <td>${page.getWebsite().getName()}</td>
                        <td>${String.valueOf(page.getWebsite().getCreated_at())}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
`)
