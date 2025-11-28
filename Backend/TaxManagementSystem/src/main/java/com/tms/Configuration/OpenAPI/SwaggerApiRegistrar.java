package com.tms.Configuration.OpenAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tms.Common.Entity.Functionalities;
import com.tms.Common.Permission.Service.FunctionalitiesService;

import java.util.*;

@Service
public class SwaggerApiRegistrar {

    @Autowired
    private FunctionalitiesService functionalitiesService;

    private static final Logger logger = LoggerFactory.getLogger(SwaggerApiRegistrar.class);

  public void registerSwaggerApis() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8443/v3/api-docs";

            Map<String, Object> openApi = restTemplate.getForObject(url, Map.class);

            if (openApi == null || !openApi.containsKey("paths")) {
                logger.warn("OpenAPI spec not found or empty.");
                System.out.println("⚠ OpenAPI spec not found or empty.");
                return;
            }

            Map<String, Object> paths = (Map<String, Object>) openApi.get("paths");

            for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();
                Map<String, Object> methods = (Map<String, Object>) pathEntry.getValue();

                for (Map.Entry<String, Object> methodEntry : methods.entrySet()) {
                    String httpMethod = methodEntry.getKey().toUpperCase();
                    Map<String, Object> methodDetails = (Map<String, Object>) methodEntry.getValue();

                    String summary = (String) methodDetails.get("summary");
                    String description = (String) methodDetails.get("description");
                    List<String> tags = (List<String>) methodDetails.get("tags");

                    String controllerName = (tags != null && !tags.isEmpty()) ? tags.get(0) : "UnknownController";
                    String fullDescription = ((description != null) ? description : "") + " (Controller: "
                            + controllerName + ")";

                    String category = detectCategory(summary, description, path);

                    if (!functionalitiesService.existsByNameAndMethod(path, httpMethod)) {
                        Functionalities f = new Functionalities();
                        f.setName(path); 
                        f.setDescription(fullDescription);
                        f.setMethod(httpMethod);
                        f.setCategory(category);

                        functionalitiesService.createFunctionality(f);

                        // Display on console
                        System.out.println("✅ Registered API: [" + httpMethod + "] " + path);
                        System.out.println("   Description: " + fullDescription);
                        System.out.println("   Category: " + category + "\n");

                        // Log as well
                        logger.info("Registered API: [{}] {} - {}", httpMethod, path, fullDescription);
                    }
                }
            }

            logger.info("✅ Swagger API endpoints successfully registered.");
            System.out.println("✅ Swagger API endpoints successfully registered.");

        } catch (Exception e) {
            logger.error("❌ Error registering Swagger API: {}", e.getMessage(), e);
            System.out.println("❌ Error registering Swagger API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String detectCategory(String summary, String description, String path) {
        String combined = (summary + " " + description + " " + path).toLowerCase();
        if (combined.contains("admin") || combined.contains("setting") || combined.contains("permission") || combined.contains("menu")) {
            return "ADMIN";
        } else if (combined.contains("/maker")) {
            return "MAKER";
        } else if (combined.contains("/reviewer")) {
            return "REVIEWER";
        } else if (combined.contains("/approver")) {
            return "APPROVER";
        } 
        return "ALL";
    }
}
