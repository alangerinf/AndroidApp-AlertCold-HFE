package ibao.alanger.alertcoldhfe.model;

import java.io.Serializable;

public class Variedad implements Serializable {
    String id;
    String name;
    public Variedad() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
