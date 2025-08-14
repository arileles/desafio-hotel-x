package com.hotel.x.hotel_x.domain.Hospede.dto;

import com.hotel.x.hotel_x.domain.Hospede.Hospede;

public record HospedeListagem(    String nome,
        String telefone, String cpf) {

    public HospedeListagem(Hospede hospede){
        this(hospede.getNome(), hospede.getTelefone(), hospede.getCpf());
    }

}
