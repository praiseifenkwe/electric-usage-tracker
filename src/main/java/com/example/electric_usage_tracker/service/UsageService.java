package com.example.electric_usage_tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.electric_usage_tracker.model.ApplianceUsage;
import com.example.electric_usage_tracker.repository.ApplianceUsageRepository;

@Service
public class UsageService {
    
    @Autowired
    private ApplianceUsageRepository repository;
    
    public ApplianceUsage saveUsage(ApplianceUsage usage) {
        usage.recalculate();
        return repository.save(usage);
    }
    
    public List<ApplianceUsage> getAllUsages() {
        return repository.findAllByOrderByRecordedAtDesc();
    }
    
    public ApplianceUsage getUsageById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public void deleteUsage(Long id) {
        repository.deleteById(id);
    }
    
    public void deleteAllUsages() {
        repository.deleteAll();
    }
    
    public double getTotalEnergy() {
        Double total = repository.getTotalEnergy();
        return total != null ? total : 0;
    }
    
    public double getTotalCost() {
        Double total = repository.getTotalCost();
        return total != null ? total : 0;
    }
}