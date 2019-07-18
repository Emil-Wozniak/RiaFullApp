package org.ria.ifzz.RiaApp.configurations.profiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@PropertySource("classpath:/prod-database-config.properties")
public class ProdDataSourceConfig {

    @Value("${production_info}")
    private String production_info;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Bean
    public void getProdInfo() {
        LOGGER.info(production_info);
    }
}

