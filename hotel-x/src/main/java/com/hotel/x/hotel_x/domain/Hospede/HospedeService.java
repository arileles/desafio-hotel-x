package com.hotel.x.hotel_x.domain.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.dto.HospedeEntradaDTO;
import com.hotel.x.hotel_x.domain.Hospede.dto.HospedeListagemDTO;
import com.hotel.x.hotel_x.domain.Hospede.exceptions.HospedeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Hospede verificarHospede(HospedeEntradaDTO hospede) {
        if (hospedeRepository.existsByCpf(hospede.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Hospede hospedeEntity = Hospede.builder()
                .cpf(hospede.getCpf())
                .nome(hospede.getNome())
                .telefone(hospede.getTelefone())
                .build();

        return hospedeRepository.save(hospedeEntity);
    }

    public void deletarHospede(Long id) {
        Hospede hospede = hospedeRepository.findById(id).orElseThrow(() -> new HospedeNotFoundException("Hospede com ID " + id + " não encontrado"));
        hospedeRepository.delete(hospede);
    }

    public Page<HospedeListagemDTO> buscarTodosOsHospedes(Pageable pageable) {
        var hospedes  = hospedeRepository.findAll(pageable);
        Page<HospedeListagemDTO> dto = hospedes.map(HospedeListagemDTO::new);
        return dto;
    }
}
