package com.example.autoplaces.pojos;

public class Place {

    private String name;
    private String vicinity;
    private GeoMetry geometry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public GeoMetry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeoMetry geometry) {
        this.geometry = geometry;
    }
}
