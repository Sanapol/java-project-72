package hexlet.code.websites;

import hexlet.code.model.Website;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class WebsitesPage extends BasePage {
    private List<Website> websites;
}
