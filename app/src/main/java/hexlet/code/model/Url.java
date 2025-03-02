package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class Url {
    private long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp lastCheck;
    private Integer code;
    private List<UrlCheck> urlCheck;

    public Url(String name) {
        this.name = name;
    }

    public final String getLastCheck() {
        if (lastCheck != null) {
            return String.valueOf(lastCheck).substring(0, 16);
        } else {
            return "";
        }
    }
}

