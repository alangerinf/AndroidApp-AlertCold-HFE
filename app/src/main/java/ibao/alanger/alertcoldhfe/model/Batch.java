package ibao.alanger.alertcoldhfe.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Batch implements Serializable {
    Area area;
    Zone zone;
    Product product;
    String nameBatch;
    String dateStart;
    String timeStart;
    String dateEnd;
    String timeEnd;
    int numPallets;
    List<Pallet> palletList;

    public Batch() {
        this.palletList = new ArrayList<>();
        this.product = new Product();
        this.area = new Area();
        this.zone = new Zone();
        this.nameBatch = "";
        this.dateStart = "";
        this.timeStart = "";
        this.dateEnd = "";
        this.timeEnd = "";
    }

    public String toString(){
        Gson gson = new Gson();

        return gson.toJson(
                this,
                new TypeToken<Batch>() {}.getType());
    }

    public List<Pallet> getPalletList() {
        return palletList;
    }

    public void setPalletList(List<Pallet> palletList) {
        this.palletList = palletList;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getNameBatch() {
        return nameBatch;
    }

    public void setNameBatch(String nameBatch) {
        this.nameBatch = nameBatch;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dataEnd) {
        this.dateEnd = dataEnd;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getNumPallets() {
        return numPallets;
    }

    public void setNumPallets(int numPallets) {
        this.numPallets = numPallets;
    }
}
