package com.hotel.x.hotel_x.domain.Hospede.dto;

import com.hotel.x.hotel_x.domain.Hospede.Hospede;

public record HospedeListagemDTO(String nome,
                                 String telefone, String cpf, Double valorTotalGasto, Double valorUltimaHospedagem) {

    public HospedeListagemDTO(Hospede hospede){
        this(hospede.getNome(), hospede.getTelefone(), hospede.getCpf(), hospede.getValorTotalGasto(), hospede.getValorUltimaHospedagem());
    }

}
