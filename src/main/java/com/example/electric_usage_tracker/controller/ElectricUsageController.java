    package com.example.electric_usage_tracker.controller;

    import com.example.electric_usage_tracker.model.ApplianceUsage;
    import com.example.electric_usage_tracker.model.CustomUserDetails;
    import com.example.electric_usage_tracker.model.User;
    import com.example.electric_usage_tracker.service.ApplianceService;
    import com.example.electric_usage_tracker.service.UsageService;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    public class ElectricUsageController {

        private final UsageService usageService;
        private final ApplianceService applianceService;

        public ElectricUsageController(UsageService usageService, ApplianceService applianceService) {
            this.usageService = usageService;
            this.applianceService = applianceService;
        }

        @GetMapping("/")
        public String home(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
            if (userDetails != null) {
                // Determine display name: use name if available, else fall back to username (email)
                String displayName = userDetails.getName() != null && !userDetails.getName().isEmpty() 
                    ? userDetails.getName() 
                    : userDetails.getUsername();
                model.addAttribute("displayName", displayName);
            
                User user = userDetails.getUser();
                model.addAttribute("usage", new ApplianceUsage());
                model.addAttribute("appliances", applianceService.getAllAppliances());
                model.addAttribute("usageHistory", usageService.getAllUsages(user));
                model.addAttribute("totalEnergy", usageService.getTotalEnergy(user));
                model.addAttribute("totalCost", usageService.getTotalCost(user));
                model.addAttribute("recordCount", usageService.getAllUsages(user).size());
            }
            return "index";
        }

    @PostMapping("/calculate")
    public String calculateUsage(@ModelAttribute ApplianceUsage usage,
                                @RequestParam(required = false) String selectedAppliance,
                                @RequestParam(required = false) String customAppliance,
                                @RequestParam String timeUnit,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        if ("custom".equals(selectedAppliance)) {
            usage.setApplianceName(customAppliance);
        } else if (selectedAppliance != null && !selectedAppliance.isEmpty()) {
            usage.setApplianceName(selectedAppliance);
            var appliance = applianceService.getApplianceByName(selectedAppliance);
            if (appliance != null) {
                usage.setWattage(appliance.getWattage());
            }
        }
        usage.setTimeUnit(timeUnit);
        usage.setUser(user);
        usageService.saveUsage(usage);
        return "redirect:/?view=results";
    }

    @PostMapping("/delete/{id}")
    public String deleteUsage(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        ApplianceUsage usage = usageService.getUsageById(id);
        if (usage != null && usage.getUser().getId().equals(user.getId())) {
            usageService.deleteUsage(id);
        }
        return "redirect:/?view=history";
    }

    @PostMapping("/delete-all")
    public String deleteAllUsages(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<ApplianceUsage> usages = usageService.getAllUsages(user);
        for (ApplianceUsage usage : usages) {
            usageService.deleteUsage(usage.getId());
        }
        return "redirect:/?view=history";
    }
}