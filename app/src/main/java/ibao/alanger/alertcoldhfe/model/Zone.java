package ibao.alanger.alertcoldhfe.model;

import java.io.Serializable;

public class Zone implements Serializable {
    String id;
    String name;
    String idArea;

    public Zone() {
        name="";
    }

    public Zone(String id, String name,String idArea) {
        this.id = id;
        this.name = name;
        this.idArea = idArea;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
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
