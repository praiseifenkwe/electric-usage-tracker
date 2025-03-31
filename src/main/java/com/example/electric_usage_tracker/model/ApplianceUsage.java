package com.example.electric_usage_tracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ApplianceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String applianceName;
    private double wattage;
    private double hoursUsed;
    private String timeUnit; // New field for time unit (e.g., "hours", "days", "weeks", "months")
    private double kWhConsumed;
    private double cost;
    private LocalDateTime recordedAt;

    // Default constructor
    public ApplianceUsage() {
        this.recordedAt = LocalDateTime.now();
        this.timeUnit = "hours"; // Default to hours
    }

    // Constructor with parameters
    public ApplianceUsage(String applianceName, double wattage, double hoursUsed, String timeUnit) {
        this.applianceName = applianceName;
        this.wattage = wattage;
        this.hoursUsed = hoursUsed;
        this.timeUnit = timeUnit != null ? timeUnit : "hours";
        this.recordedAt = LocalDateTime.now();
        recalculate(); // Calculate kWh and cost upon creation
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplianceName() {
        return applianceName;
    }

    public void setApplianceName(String applianceName) {
        this.applianceName = applianceName;
    }

    public double getWattage() {
        return wattage;
    }

    public void setWattage(double wattage) {
        this.wattage = wattage;
    }

    public double getHoursUsed() {
        return hoursUsed;
    }

    public void setHoursUsed(double hoursUsed) {
        this.hoursUsed = hoursUsed;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public double getKWhConsumed() {
        return kWhConsumed;
    }

    public void setKWhConsumed(double kWhConsumed) {
        this.kWhConsumed = kWhConsumed;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    // Method to recalculate consumption and cost based on time unit
    public void recalculate() {
        double hours;
        switch (this.timeUnit != null ? this.timeUnit : "hours") {
            case "days":
                hours = this.hoursUsed * 24;
                break;
            case "weeks":
                hours = this.hoursUsed * 24 * 7;
                break;
            case "months":
                hours = this.hoursUsed * 24 * 30; // Approximation for a month
                break;
            default: // hours
                hours = this.hoursUsed;
                break;
        }
        this.kWhConsumed = (this.wattage * hours) / 1000;
        this.cost = this.kWhConsumed * 70;
    }
}