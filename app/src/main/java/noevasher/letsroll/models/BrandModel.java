package noevasher.letsroll.models;

import java.util.ArrayList;

public class BrandModel {
    private int id;
    private String name;
    private ArrayList brands;

    public BrandModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getBrands() {
        return brands;
    }

    public void setBrands(ArrayList brands) {
        this.brands = brands;
    }
}
