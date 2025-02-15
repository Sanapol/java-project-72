package hexlet.code.websites;

import io.javalin.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BuildWebsitePage {
    private String name;
    private Map<String, List<ValidationError<Object>>> errors;
}
