package com.nimbus.nimbusWebServer.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.nimbus.nimbusWebServer.config.properties.MercadoPagoProperties;
import com.nimbus.nimbusWebServer.dtos.CheckoutMercadoPagoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MercadoPagoService {

    @Autowired
    private MercadoPagoProperties mpConfig;

    public void finalizarCompraMp(CheckoutMercadoPagoRequestDto checkoutMpDto) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(mpConfig.accessToken());
    }
}
