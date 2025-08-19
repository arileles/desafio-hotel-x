package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemListarDTO;
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
    HospedagemRepository hospedagemRepository;
    @Autowired
    private HospedeRepository hospedeRepository;
    @Autowired
    private CalculoHospedagemService calculoHospedagemService;

    public Page<Hospedagem> hospedesAtivos() {
        Page<Hospedagem> hospedagensAtivas  = hospedagemRepository.findByDataSaidaIsNull(PageRequest.of(0, 10));
        return hospedagensAtivas;
    }

    public HospedagemEntradaDTO conferirHospedagem(HospedagemEntradaDTO hospedagem) {
        if (hospedagem.getDataEntrada()!= null && hospedagem.getDataEntrada().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Favor não informar uma data de entrada futura");
        }
        if (hospedagem.getDataSaida()!= null && hospedagem.getDataSaida().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Favor não informar uma data de saída futura");
        }

        return hospedagem;
    }

    public Hospedagem salvarHospedagem(HospedagemEntradaDTO hospedagem) {
        Hospede hospede = hospedeRepository.findByCpf(hospedagem.getHospede()).orElseThrow(() -> new RuntimeException("Hóspede não encontrado"));
        Hospedagem hospedagemEntity = new Hospedagem();

        if (hospedagem.getDataSaida()!= null && hospedagem.getDataEntrada()== null){
            throw new RuntimeException("Favor adicionar uma data de entrada para ser possível adicionar uma saída");
        }
        if (hospedagem.getDataSaida()!= null && hospedagem.getDataSaida().isBefore(hospedagem.getDataEntrada())){
            throw new RuntimeException("Favor adicionar uma data de entrada anterior a data de saída");
        }
        hospedagemEntity.setHospede(hospede);
        hospedagemEntity.setDataEntrada(hospedagem.getDataEntrada());
        hospedagemEntity.setDataSaida(hospedagem.getDataSaida());
        hospedagemEntity.setAdicionalVeiculo(hospedagem.isAdicionalVeiculo());
        hospedagemEntity.setValorTotal(hospedagem.getValorTotal());
        hospedagemEntity.setObservacoes(hospedagem.getObservacoes());
        hospedagemRepository.save(hospedagemEntity);

        if (hospedagem.getDataEntrada()!= null && hospedagem.getDataSaida()!= null){
            hospedagem.setValorTotal(calculoHospedagemService.calcular(hospedagemEntity));
        }

        return hospedagemEntity;
    }

    public Page<Hospedagem> hospedesInativos() {
        Page<Hospedagem> hospedagensInativas  = hospedagemRepository.findByDataSaidaIsNotNull(PageRequest.of(0, 10));
        return hospedagensInativas;
    }

    public HospedagemListarDTO atualizarHospedagem(HospedagemEntradaDTO hospedagemEntradaDTO, Long id) {
        Hospedagem hospedagem =  hospedagemRepository.findById(id).orElseThrow(()-> new RuntimeException("Não existe hospedagem com esse número"));

        if (hospedagemEntradaDTO.getDataSaida()!= null &&( hospedagem.getDataEntrada() == null && hospedagemEntradaDTO.getDataEntrada() == null)){
            throw new RuntimeException("Favor adicionar uma data de entrada para ser possível adicionar uma saída");
        }
        if (hospedagemEntradaDTO.getDataSaida()!= null && (hospedagemEntradaDTO.getDataSaida().isBefore(hospedagem.getDataEntrada()) && hospedagemEntradaDTO.getDataSaida().isBefore(hospedagemEntradaDTO.getDataEntrada()))){
            throw new RuntimeException("Favor adicionar uma data de entrada anterior a data de saída");
        }
        hospedagemEntradaDTO = conferirHospedagem(hospedagemEntradaDTO);

        if (hospedagemEntradaDTO.isAdicionalVeiculo()){
            hospedagem.setAdicionalVeiculo(hospedagemEntradaDTO.isAdicionalVeiculo());
        }
        if (hospedagemEntradaDTO.getDataEntrada()!= null){
            hospedagem.setDataEntrada(hospedagemEntradaDTO.getDataEntrada());
        }
        if (hospedagemEntradaDTO.getDataSaida()!= null){
            hospedagem.setDataSaida(hospedagemEntradaDTO.getDataSaida());
        }
        if (hospedagemEntradaDTO.getObservacoes()!= null){
            hospedagem.setObservacoes(hospedagemEntradaDTO.getObservacoes());
        }

        if (hospedagem.getDataEntrada()!= hospedagemEntradaDTO.getDataEntrada() || hospedagem.getDataSaida()!= hospedagemEntradaDTO.getDataSaida()){
            hospedagem.setValorTotal(calculoHospedagemService.calcular(hospedagem));
        }
        hospedagemRepository.save(hospedagem);
        HospedagemListarDTO dto =  new HospedagemListarDTO(hospedagem);
        return dto;

    }
    public void excluirHospedagem(Long id) {
        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedagem não encontrada com o ID: " + id));
        hospedagemRepository.delete(hospedagem);
    }
}
