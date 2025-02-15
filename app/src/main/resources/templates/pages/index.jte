@import hexlet.code.utilit.NamedRoutes
@import hexlet.code.websites.BuildWebsitePage
@import io.javalin.validation.Validation
@import io.javalin.validation.ValidationError
@import java.net.URI
@import java.util.List

@param BuildWebsitePage page

@template.page(content = @`
    <div>
        <form action="${NamedRoutes.urlsPage()}" method="post">
            <div>
                <label>
                    Название сайта
                    <input type="url" name="name" value="${page.getName()}">
                </label>
                @if (page.getErrors() != null)
                    <ul>
                        @for (List<ValidationError<Object>> errors : page.getErrors().values())
                            @for (ValidationError<Object> error : errors)
                                <li>
                                    ${error.getMessage()   }
                                </li>
                            @endfor
                        @endfor
                    </ul>
                @endif
                <input type="submit" value="принять" class="btn btn-primary">
            </div>
        </form>
    </div>
`)
