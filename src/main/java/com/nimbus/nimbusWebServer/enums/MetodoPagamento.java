package com.nimbus.nimbusWebServer.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetodoPagamento {
    CREDIT_CARD("CREDIT_CARD", "credit_card", null),
    PIX("PIX", "bank_transfer", "pix"),
    BOLETO("BOLETO", "ticket", "boleto");

    private final String valor;
    private final String mpType;
    private final String mpId;

    MetodoPagamento(String valor, String mpType, String mpId) {
        this.valor = valor;
        this.mpType = mpType;
        this.mpId = mpId; // null = usa o id vindo do DTO (bandeira do cartão)
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    public String getMpType() { return mpType; }
    public String getMpId() { return mpId; }

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
