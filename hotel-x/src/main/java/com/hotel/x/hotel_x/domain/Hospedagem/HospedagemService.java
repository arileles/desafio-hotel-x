package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.HospedeRepository;

import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HospedagemService {
    @Autowired
    private HospedagemRepository hospedagemRepository;
    @Autowired
    private HospedeRepository hospedeRepository;

    public Page<Hospedagem> hospedesAtivos() {
        Page<Hospedagem> hospedagensAtivas  = hospedagemRepository.findByDataSaidaIsNotNull(PageRequest.of(0, 10));
        return hospedagensAtivas;
    }

    public Hospedagem salvarHospedagem(HospedagemEntradaDTO hospedagem) {
        if (hospedagem.getDataEntrada()!= null && hospedagem.getDataEntrada().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Favor não informar uma data de entrada futura");
        }
        if (hospedagem.getDataSaida()!= null && hospedagem.getDataSaida().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Favor não informar uma data de saída futura");
        }
        Hospede hospede = hospedeRepository.findByCpf(hospedagem.getHospede().toString()).orElseThrow(() -> new RuntimeException("Hóspede não encontrado"));
        Hospedagem hospedagemEntity = new Hospedagem();
        hospedagemEntity.setHospede(hospede);
        hospedagemEntity.setDataEntrada(hospedagem.getDataEntrada());
        hospedagemEntity.setDataSaida(hospedagem.getDataSaida());
        hospedagemEntity.setAdicionalVeiculo(hospedagem.isAdicionalVeiculo());
        hospedagemEntity.setValorTotal(hospedagem.getValorTotal());
        hospedagemEntity.setObservacoes(hospedagem.getObservacoes());
        hospedagemRepository.save(hospedagemEntity);
        return hospedagemEntity;
    }

    public Page<Hospedagem> hospedesInativos() {
        Page<Hospedagem> hospedagensInativas  = hospedagemRepository.findByDataSaidaIsNull(PageRequest.of(0, 10));
        return hospedagensInativas;
    }
}
