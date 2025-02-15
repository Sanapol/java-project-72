package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Website {
    private long id;
    private String name;
    private Timestamp created_at;

    public Website(String name) {
        this.name = name;
    }
}
