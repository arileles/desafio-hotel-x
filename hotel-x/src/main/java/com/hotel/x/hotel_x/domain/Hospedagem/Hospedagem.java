package com.hotel.x.hotel_x.domain.Hospedagem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "Hospedagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "hospedagem")
public class Hospedagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hospede")
    private Hospede hospede;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataEntrada;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime  dataSaida;
    @NotNull
    private boolean adicionalVeiculo;
    @NotNull
    private Double valorTotal;
    private String observacoes;
}
