package com.hotel.x.hotel_x.domain.Hospedagem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Hospedagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "hospedagem")
@Builder
public class Hospedagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hospede")
    private Hospede hospede;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime dataEntrada;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime  dataSaida;
    @NotNull
    private Boolean adicionalVeiculo;
    private Double valorTotal;
    private String observacoes;
}
