package ibao.alanger.alertcoldhfe.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pallet implements Serializable {
    int position;
    String cod;
    boolean isChecked;
    List<Sensor> sensorList;
    public Pallet(int pos) {
        this.sensorList = new ArrayList<>();
        this.cod= "";
        this.position=pos;
        this.isChecked=false;
    }

    public String toString(){
        Gson gson = new Gson();

        return gson.toJson(
                this,
                new TypeToken<Pallet>() {}.getType());
    }
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }


}
