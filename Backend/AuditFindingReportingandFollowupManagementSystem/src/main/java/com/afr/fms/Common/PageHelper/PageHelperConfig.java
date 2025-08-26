package com.afr.fms.Common.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class PageHelperConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            PageInterceptor pageInterceptor = new PageInterceptor();
            Properties properties = new Properties();
            properties.setProperty("helperDialect", "sqlserver"); // Change to your DB type (mysql, postgresql, etc.)
            properties.setProperty("reasonable", "true");
            properties.setProperty("supportMethodsArguments", "true");
            pageInterceptor.setProperties(properties);
            configuration.addInterceptor(pageInterceptor);
        };
    }
}
