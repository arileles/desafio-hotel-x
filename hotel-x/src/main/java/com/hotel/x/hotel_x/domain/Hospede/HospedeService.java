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

    public Hospede atualizarHospede(Hospede hospede) {
        Hospede salvo = findByDocumentoHospede(hospede.getCpf());

        if (hospede.getNome() != null){
             salvo.setNome(hospede.getNome());
        }
        if (hospede.getTelefone() != null){
            salvo.setTelefone(hospede.getTelefone());
        }
        if (!hospede.getTelefone().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("O telefone deve conter 10 ou 11 dígitos numéricos");
        }
        hospedeRepository.save(salvo);

        return salvo;
    }

    public Hospede verificacaoHospede(Hospede hospede) {
        if (hospedeRepository.existsByCpf(hospede.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        return hospedeRepository.save(hospede);
    }
}
