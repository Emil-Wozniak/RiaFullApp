package org.ria.ifzz.RiaApp.configurations.profiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("dev")
@PropertySource("classpath:/application.properties")
public class DevDataSourceConfig {

    @Value("${development_info}")
    private String development_info;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Bean
    public void getDevInfo() {
        LOGGER.info(development_info);
    }
}
