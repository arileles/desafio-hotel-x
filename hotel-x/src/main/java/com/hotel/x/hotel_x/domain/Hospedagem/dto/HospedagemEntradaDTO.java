package com.hotel.x.hotel_x.domain.Hospedagem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.x.hotel_x.domain.Hospedagem.Hospedagem;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class HospedagemEntradaDTO {
    private String hospede;
    private LocalDateTime dataEntrada;
    private LocalDateTime  dataSaida;
    private boolean adicionalVeiculo;
    private Double valorTotal;
    private String observacoes;
}

