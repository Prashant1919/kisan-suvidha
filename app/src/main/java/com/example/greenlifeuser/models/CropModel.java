package com.example.greenlifeuser.models;

public class CropModel {

    private String cid, name, nitrogen, phosphorus, potassium, temperature, humidity, pH, rainfall, image;

    public CropModel() {
    }

    public CropModel(String cid, String name, String nitrogen, String phosphorus, String potassium, String temperature, String humidity, String pH, String rainfall, String image) {
        this.cid = cid;
        this.name = name;
        this.nitrogen = nitrogen;
        this.phosphorus = phosphorus;
        this.potassium = potassium;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pH = pH;
        this.rainfall = rainfall;
        this.image = image;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(String nitrogen) {
        this.nitrogen = nitrogen;
    }

    public String getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(String phosphorus) {
        this.phosphorus = phosphorus;
    }

    public String getPotassium() {
        return potassium;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getpH() {
        return pH;
    }

    public void setpH(String pH) {
        this.pH = pH;
    }

    public String getRainfall() {
        return rainfall;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
