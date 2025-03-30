package com.example.electric_usage_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.electric_usage_tracker.model.ApplianceUsage;

public interface ApplianceUsageRepository extends JpaRepository<ApplianceUsage, Long> {
    
    List<ApplianceUsage> findAllByOrderByRecordedAtDesc();
    
    @Query("SELECT SUM(a.kWhConsumed) FROM ApplianceUsage a")
    Double getTotalEnergy();
    
    @Query("SELECT SUM(a.cost) FROM ApplianceUsage a")
    Double getTotalCost();
}