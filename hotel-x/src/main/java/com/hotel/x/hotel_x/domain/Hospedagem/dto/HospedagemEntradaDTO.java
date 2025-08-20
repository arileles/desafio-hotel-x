package com.hotel.x.hotel_x.domain.Hospedagem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.x.hotel_x.domain.Hospedagem.Hospedagem;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class HospedagemEntradaDTO {
    private String hospede;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime dataEntrada;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime  dataSaida;
    private boolean adicionalVeiculo;
    private String observacoes;
}

