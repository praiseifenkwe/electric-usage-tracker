package com.example.electric_usage_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.electric_usage_tracker.model.ApplianceUsage;
import com.example.electric_usage_tracker.service.ApplianceService;
import com.example.electric_usage_tracker.service.UsageService;

@Controller
public class ElectricUsageController {
    
    @Autowired
    private UsageService usageService;
    
    @Autowired
    private ApplianceService applianceService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("usage", new ApplianceUsage());
        model.addAttribute("appliances", applianceService.getAllAppliances());
        model.addAttribute("usageHistory", usageService.getAllUsages());
        model.addAttribute("totalEnergy", usageService.getTotalEnergy());
        model.addAttribute("totalCost", usageService.getTotalCost());
        model.addAttribute("recordCount", usageService.getAllUsages().size());
        return "index";
    }
    
    @PostMapping("/calculate")
    public String calculateUsage(@ModelAttribute ApplianceUsage usage, 
                                @RequestParam(required = false) String selectedAppliance,
                                @RequestParam(required = false) String customAppliance) {
        
        // Set appliance name based on form inputs
        if ("custom".equals(selectedAppliance)) {
            usage.setApplianceName(customAppliance);
        } else if (selectedAppliance != null && !selectedAppliance.isEmpty()) {
            usage.setApplianceName(selectedAppliance);
            // If a predefined appliance was selected, set its standard wattage
            var appliance = applianceService.getApplianceByName(selectedAppliance);
            if (appliance != null) {
                usage.setWattage(appliance.getWattage());
            }
        }
        
        // Calculate consumption and save
        usageService.saveUsage(usage);
        return "redirect:/?view=results";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUsage(@PathVariable Long id) {
        usageService.deleteUsage(id);
        return "redirect:/?view=history";
    }
    
    @GetMapping("/delete-all")
    public String deleteAllUsages() {
        usageService.deleteAllUsages();
        return "redirect:/?view=history";
    }
}