package com.nimbus.nimbusWebServer.models.pedido;

import com.nimbus.nimbusWebServer.enums.MetodoPagamento;
import com.nimbus.nimbusWebServer.enums.StatusPedido;
import com.nimbus.nimbusWebServer.enums.TipoDocumentoComprador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PEDIDO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "numero_pedido", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_numero_pedido")
    @SequenceGenerator(name = "seq_numero_pedido", sequenceName = "numero_pedido_seq", allocationSize = 1)
    private Long numeroPedido;

    private String servicoPagamento;

    @Column(name = "order_id", unique = true)
    private String orderId;

    private String paymentId;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    private String statusDetalhe;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private Integer parcelas;
    private String bandeira;

    private String compradorNome;
    private String compradorEmail;

    @Enumerated(EnumType.STRING)
    private TipoDocumentoComprador compradorTipoDocumento;

    private String compradorNumeroDocumento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoItem> itens = new ArrayList<>();

    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant pagoEm;
    private Instant expiraEm;

}

