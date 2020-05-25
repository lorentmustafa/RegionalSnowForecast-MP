package com.fiek.regionalsnowforecast;

public class Resorts {
    private int rId;
    private String name;
    private String location;
    private int rImage;

    public Resorts() {
        super();
    }

    public Resorts(int rId, String name, String location, int rImage) {
        super();
        this.rId = rId;
        this.name = name;
        this.location = location;
        this.rImage = rImage;
    }

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getrImage() {
        return rImage;
    }

    public void setrImage(int rImage) {
        this.rImage = rImage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Resorts other = (Resorts) obj;
        if (rId != other.rId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Resort [id=" + rId + ", name=" + name + ", location="
                + location + "]";
    }
}
