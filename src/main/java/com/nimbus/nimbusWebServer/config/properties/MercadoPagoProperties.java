package com.nimbus.nimbusWebServer.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mercadopago")
public record MercadoPagoProperties(
   String accessToken,
   String webhookSecret
) {}
