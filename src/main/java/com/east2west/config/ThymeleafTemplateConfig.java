package com.east2west.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafTemplateConfig {

    // @Value("${thymeleaf.templates.location}")
    // private String templateLocation;

    // @Value("${thymeleaf.templates.extension}")
    // private String templateExtension;

    // @Bean
    // public SpringTemplateEngine springTemplateEngine() {
    //     SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    //     templateEngine.addTemplateResolver(htmlTemplateResolver());
    //     return templateEngine;
    // }

    // @Bean
    // public SpringResourceTemplateResolver htmlTemplateResolver() {
    //     SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    //     templateResolver.setPrefix(templateLocation);
    //     templateResolver.setSuffix(templateExtension);
    //     templateResolver.setTemplateMode(TemplateMode.HTML);
    //     templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    //     return templateResolver;
    // }
}