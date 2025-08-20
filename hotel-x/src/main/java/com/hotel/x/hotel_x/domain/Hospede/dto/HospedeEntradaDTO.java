package com.hotel.x.hotel_x.domain.Hospede.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class HospedeEntradaDTO {
    private String nome;
    @Pattern(
            regexp = "\\d{10,11}",
            message = "O telefone deve conter 10 ou 11 dígitos numéricos"
    )
    private String telefone;
    @CPF
    private String cpf;
}
