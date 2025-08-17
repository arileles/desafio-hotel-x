package com.hotel.x.hotel_x.domain.Hospedagem.dto;

import com.hotel.x.hotel_x.domain.Hospedagem.Hospedagem;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;

import java.time.LocalDateTime;

public record HospedagemListarDTO(     String cpf, String nome, String telefone,
         LocalDateTime dataEntrada,
         LocalDateTime  dataSaida,
         boolean adicionalVeiculo,
         Double valorTotal,
         String observacoes) {
    public HospedagemListarDTO(Hospedagem hospedagem){
        this(hospedagem.getHospede().getCpf(),hospedagem.getHospede().getNome() ,hospedagem.getHospede().getTelefone(), hospedagem.getDataEntrada(), hospedagem.getDataSaida(),
                hospedagem.getAdicionalVeiculo(), hospedagem.getValorTotal(), hospedagem.getObservacoes());

        }
    }


