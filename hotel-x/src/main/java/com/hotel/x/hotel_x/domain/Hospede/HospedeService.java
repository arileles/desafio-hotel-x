package com.hotel.x.hotel_x.domain.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.exceptions.HospedeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HospedeService {
    @Autowired
    private HospedeRepository hospedeRepository;

    public Hospede findByDocumentoHospede(String cpf) {
        return hospedeRepository.findByCpf(cpf)
                .orElseThrow(() -> new HospedeNotFoundException(cpf));
    }

    public Hospede findByNomeHospede(String nome) {
        return hospedeRepository.findByNome(nome)
                .orElseThrow(() -> new HospedeNotFoundException(nome));
    }

    public Hospede findByTelefoneHospede(String telefone) {
        return hospedeRepository.findByTelefone(telefone)
                .orElseThrow(() -> new HospedeNotFoundException(telefone));
    }
}
