package org.example.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ThymeleafService {
    public static String checkTemplate(String templateName) {
        int maxAttempts = 5;
        long retryDelay = 1000; // milliseconds

        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                Resource templateResource = new ClassPathResource("templates/" + templateName + ".html");
                if (templateResource.exists()) {
                    return templateName;
                } else {
                    Thread.sleep(retryDelay);
                    attempts++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "user/cabinet_not_found";
    }
}
