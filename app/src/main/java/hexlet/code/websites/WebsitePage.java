package hexlet.code.websites;

import hexlet.code.model.Website;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WebsitePage extends BasePage {
    private Website website;
}
