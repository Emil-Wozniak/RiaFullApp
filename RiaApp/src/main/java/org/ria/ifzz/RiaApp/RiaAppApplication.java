package org.ria.ifzz.RiaApp;

import org.ria.ifzz.RiaApp.service.StorageProperties;
import org.ria.ifzz.RiaApp.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class RiaAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(RiaAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
			storageService.init();
		};
	}
}
