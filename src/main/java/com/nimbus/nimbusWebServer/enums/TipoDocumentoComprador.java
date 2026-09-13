package com.nimbus.nimbusWebServer.enums;

public enum TipoDocumentoComprador {
    CPF("CPF"),
    CNPJ("CNPJ");

    private final String valor;

    TipoDocumentoComprador(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoDocumentoComprador fromValor(String valor) {
        for (TipoDocumentoComprador tipo : values()) {
            if(tipo.valor.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de documento desconhecido: " + valor);
    }
}
