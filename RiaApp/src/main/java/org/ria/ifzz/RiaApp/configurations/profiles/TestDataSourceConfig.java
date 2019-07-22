package org.ria.ifzz.RiaApp.configurations.profiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("test")
@PropertySource("classpath:/test-database-config.properties")
public class TestDataSourceConfig {

    @Value("${test_info}")
    private String test_info;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Bean
    public void getTestInfo() {
        LOGGER.info(test_info);
    }
}
