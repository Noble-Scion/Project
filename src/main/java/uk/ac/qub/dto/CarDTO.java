package uk.ac.qub.dto;

public class CarDTO {
    private String registrationNumber;
    private String make;
    private String model;
    private Integer year;
    private String fuelType;
    private Double avgMpg;

    // Getters and Setters
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public Double getAvgMpg() { return avgMpg; }
    public void setAvgMpg(Double avgMpg) { this.avgMpg = avgMpg; }
}