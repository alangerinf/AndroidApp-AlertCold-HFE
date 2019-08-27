package ibao.alanger.alertcoldhfe.model;

import java.io.Serializable;

public class Area implements Serializable {
    String id;
    String name;



    public Area() {
        name="";
    }

    public Area(String id, String name) {
        this.id = id;
        this.name = name;
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
