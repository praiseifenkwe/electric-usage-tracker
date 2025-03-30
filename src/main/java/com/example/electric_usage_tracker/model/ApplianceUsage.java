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
    private double kWhConsumed;
    private double cost;
    private LocalDateTime recordedAt;

    // Default constructor
    public ApplianceUsage() {
        this.recordedAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public ApplianceUsage(String applianceName, double wattage, double hoursUsed) {
        this.applianceName = applianceName;
        this.wattage = wattage;
        this.hoursUsed = hoursUsed;
        this.recordedAt = LocalDateTime.now();
        
        // Calculate kWh: (wattage × hours) ÷ 1000
        this.kWhConsumed = (wattage * hoursUsed) / 1000;
        
        // Calculate cost: kWh × rate (assuming ₦70 per kWh)
        this.cost = this.kWhConsumed * 70;
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

    // Method to recalculate consumption and cost
    public void recalculate() {
        this.kWhConsumed = (this.wattage * this.hoursUsed) / 1000;
        this.cost = this.kWhConsumed * 70;
    }
}