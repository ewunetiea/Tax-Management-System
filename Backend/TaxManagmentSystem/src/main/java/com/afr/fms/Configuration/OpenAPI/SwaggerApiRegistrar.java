package com.afr.fms.Configuration.OpenAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.afr.fms.Common.Entity.Functionalities;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

import java.util.*;

@Service
public class SwaggerApiRegistrar {

    @Autowired
    private FunctionalitiesService functionalitiesService;

    private static final Logger logger = LoggerFactory.getLogger(SwaggerApiRegistrar.class);

    private static final List<String> CATEGORIES = Arrays.asList(
            "APPROVER_BFA", "AUDITOR_BFA", "BRANCHM_BFA", "COMPILER_BFA", "REGIONALD_BFA", "REVIEWER_BFA");

    public void registerSwaggerApis() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8442/v3/api-docs";

            Map<String, Object> openApi = restTemplate.getForObject(url, Map.class);
            if (openApi == null || !openApi.containsKey("paths")) {
                logger.warn("OpenAPI spec not found or empty.");
                return;
            }

            Map<String, Object> paths = (Map<String, Object>) openApi.get("paths");

            for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();

                // Only register paths starting with /api/branch_audit
                // if (!path.startsWith("/api/branch_audit")) {
                //     continue;
                // }

                Map<String, Object> methods = (Map<String, Object>) pathEntry.getValue();

                for (Map.Entry<String, Object> methodEntry : methods.entrySet()) {
                    String httpMethod = methodEntry.getKey().toUpperCase();
                    Map<String, Object> methodDetails = (Map<String, Object>) methodEntry.getValue();

                    String summary = (String) methodDetails.get("summary");
                    String description = (String) methodDetails.get("description");
                    List<String> tags = (List<String>) methodDetails.get("tags");

                    // Append controller name (from first tag) to the description
                    String controllerName = (tags != null && !tags.isEmpty()) ? tags.get(0) : "UnknownController";
                    String fullDescription = ((description != null) ? description : "") + " (Controller: "
                            + controllerName + ")";

                    String category = detectCategory(summary, description, path);

                    if (!functionalitiesService.existsByNameAndMethod(path, httpMethod)) {
                        Functionalities f = new Functionalities();
                        f.setName(path); // API path
                        f.setDescription(fullDescription);
                        f.setMethod(httpMethod);
                        f.setCategory(category);

                        functionalitiesService.createFunctionality(f);
                    }
                }
            }

            logger.info("✅ Swagger API endpoints successfully registered.");

        } catch (Exception e) {
            logger.error("❌ Error registering Swagger API: {}", e.getMessage(), e);
        }
    }

    private String detectCategory(String summary, String description, String path) {
        String combined = (summary + " " + description + " " + path).toLowerCase();
        if (combined.contains("admin") || combined.contains("setting") || combined.contains("role")
                || combined.contains("permission") || combined.contains("menu")) {
            return "ADMIN";
        } else if (combined.contains("/api/audit/auditor")) {
            return "AUDITOR_IS, AUDITOR_MGT";
        } else if (combined.contains("auditor")) {
            return "AUDITOR_MGT";
        } else if (combined.contains("/api/audit/approver")) {
            return "APPROVER_IS, APPROVER_MGT";
        } else if (combined.contains("approver")) {
            return "APPROVER_MGT";
        } else if (combined.contains("/api/audit/reviewer")) {
            return "REVIEWER_IS, REVIEWER_MGT";
        } else if (combined.contains("reviewer")) {
            return "REVIEWER_MGT";
        } else if (combined.contains("/api/audit/followup_officer")) {
            return "FOLLOWUP_OFFICER_IS, FOLLOWUP_OFFICER_MGT";
        } else if (combined.contains("followup_officer_enhanced")) {
            return "FOLLOWUP_OFFICER_MGT";
        } else if (combined.contains("division")) {
            return "AUDITEE_DIVISION";
        } else if (combined.contains("auditee")) {
            return "AUDITEE";
        } else if (combined.contains("mgt_report")) {
            return "ALL_MGT, AUDITEE_DIVISION, AUDITEE";
        } else if (combined.contains("is_report")) {
            return "ALL_IS";
        } else if (combined.contains("/api/audit") || combined.contains("remark")) {
            return "ALL_IS_MGT";
        } else if (combined.contains("/api/branch/dashboard/") || combined.contains("/api/branch/report/")) {
            return "HIGHER_OFFICIAL";
        }
        return "ALL";
    }
}
