package com.example.electric_usage_tracker.service;

import com.example.electric_usage_tracker.model.ApplianceUsage;
import com.example.electric_usage_tracker.model.User;
import com.example.electric_usage_tracker.repository.ApplianceUsageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsageService {

    private final ApplianceUsageRepository applianceUsageRepository;

    public UsageService(ApplianceUsageRepository applianceUsageRepository) {
        this.applianceUsageRepository = applianceUsageRepository;
    }

    public List<ApplianceUsage> getAllUsages(User user) {
        return applianceUsageRepository.findByUserOrderByRecordedAtDesc(user);
    }

    public Double getTotalEnergy(User user) {
        Double total = applianceUsageRepository.getTotalEnergyByUser(user);
        return total != null ? total : 0.0;
    }

    public Double getTotalCost(User user) {
        Double total = applianceUsageRepository.getTotalCostByUser(user);
        return total != null ? total : 0.0;
    }

    public void saveUsage(ApplianceUsage usage) {
        usage.recalculate();
        applianceUsageRepository.save(usage);
    }

    public ApplianceUsage getUsageById(Long id) {
        return applianceUsageRepository.findById(id).orElse(null);
    }

    public void deleteUsage(Long id) {
        applianceUsageRepository.deleteById(id);
    }

    public void deleteAllUsages() {
        applianceUsageRepository.deleteAll();
    }
}