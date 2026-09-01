package com.nimbus.nimbusWebServer.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetodoPagamento {
    CREDIT_CARD("CREDIT_CARD"),
    PIX("PIX"),
    BOLETO("BOLETO");

    private final String valor;

    MetodoPagamento(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static MetodoPagamento fromValor(String valor) {
        for (MetodoPagamento metodo : values()) {
            if(metodo.valor.equals(valor)) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Metodo de pagamento desconhecido: " + valor);
    }

    public static MetodoPagamento tranformaMetodoPagamento(String metodoPagamento) {
        metodoPagamento = metodoPagamento.toUpperCase().strip();
        return switch (metodoPagamento) {
            case "CREDIT_CARD" -> CREDIT_CARD;
            case "PIX" -> PIX;
            case "BOLETO" -> BOLETO;
            default -> throw new IllegalArgumentException("Método desconhecido:" + metodoPagamento);
        };
    }
}
