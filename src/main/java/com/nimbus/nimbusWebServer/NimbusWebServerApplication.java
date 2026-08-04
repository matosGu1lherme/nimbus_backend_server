package com.nimbus.nimbusWebServer;

import com.nimbus.nimbusWebServer.config.properties.MercadoPagoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EnableConfigurationProperties(MercadoPagoProperties.class)
public class NimbusWebServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NimbusWebServerApplication.class, args);
	}

}
