package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemListarDTO;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;

import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import com.hotel.x.hotel_x.domain.Hospede.HospedeService;
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
    HospedeService hospedeService;

    @Autowired
    private CalculoHospedagemService calculoHospedagemService;

    public Page<Hospedagem> hospedesAtivos() {
        return hospedagemRepository.findByDataSaidaIsNull(PageRequest.of(0, 100));
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
        Hospede hospede = hospedeService.findByDocumentoHospede(hospedagem.getHospede());
        Hospedagem hospedagemEntity = new Hospedagem();

        if (hospedagem.getDataSaida()!= null && hospedagem.getDataEntrada()== null){
            throw new RuntimeException("Favor adicionar uma data de entrada para ser possível adicionar uma saída");
        }

        if (hospedagem.getDataSaida()!= null && hospedagem.getDataSaida().isBefore(hospedagem.getDataEntrada())){
            throw new RuntimeException("Favor adicionar uma data de entrada anterior a data de saída");
        }

         hospedagemEntity = Hospedagem.builder()
                .hospede(hospede)
                .dataEntrada(hospedagem.getDataEntrada())
                .dataSaida(hospedagem.getDataSaida())
                .adicionalVeiculo(hospedagem.isAdicionalVeiculo())
                .observacoes(hospedagem.getObservacoes())
                .build();

        if (hospedagem.getDataEntrada()!= null && hospedagem.getDataSaida()!= null){
            Double calculoHospedagem = calculoHospedagemService.calcular(hospedagemEntity);
            hospedagemEntity.setValorTotal(calculoHospedagem);
            atualizarValoresHospede(hospede, calculoHospedagem);
        }

        hospedagemRepository.save(hospedagemEntity);

        return hospedagemEntity;
    }

    private void atualizarValoresHospede(Hospede hospede, Double calculoHospedagem) {
        Double valorAtual = hospede.getValorTotalGasto();

        if (valorAtual == null) {
            valorAtual = 0.0;
        }

        hospede.setValorTotalGasto(valorAtual+calculoHospedagem);
        hospede.setValorUltimaHospedagem(calculoHospedagem);
        hospedeService.atualizarHospede(hospede);
    }

    public Page<Hospedagem> hospedesInativos() {
        return hospedagemRepository.findByDataSaidaIsNotNull(PageRequest.of(0, 100));
    }

    public HospedagemListarDTO atualizarHospedagem(HospedagemEntradaDTO hospedagemEntradaDTO, Long id) {
        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe hospedagem com esse número"));

        validarEntradaESaida(hospedagemEntradaDTO, hospedagem);
        hospedagemEntradaDTO = conferirHospedagem(hospedagemEntradaDTO);

        atualizarCamposHospedagem(hospedagem, hospedagemEntradaDTO);
        atualizarValorTotalSeNecessario(hospedagem, hospedagemEntradaDTO);

        hospedagemRepository.save(hospedagem);

        return new HospedagemListarDTO(hospedagem);
    }

    private void validarEntradaESaida(HospedagemEntradaDTO dto, Hospedagem hospedagem) {
        if (dto.getDataSaida() != null && (hospedagem.getDataEntrada() == null && dto.getDataEntrada() == null)) {
            throw new RuntimeException("Favor adicionar uma data de entrada para ser possível adicionar uma saída");
        }

        if (dto.getDataSaida() != null &&
                (dto.getDataSaida().isBefore(hospedagem.getDataEntrada()) &&
                        dto.getDataSaida().isBefore(dto.getDataEntrada()))) {
            throw new RuntimeException("Favor adicionar uma data de entrada anterior a data de saída");
        }
    }

    private void atualizarCamposHospedagem(Hospedagem hospedagem, HospedagemEntradaDTO dto) {
        if (dto.isAdicionalVeiculo()) {
            hospedagem.setAdicionalVeiculo(true);
        }
        if (dto.getDataEntrada() != null) {
            hospedagem.setDataEntrada(dto.getDataEntrada());
        }
        if (dto.getDataSaida() != null) {
            hospedagem.setDataSaida(dto.getDataSaida());
        }
        if (dto.getObservacoes() != null) {
            hospedagem.setObservacoes(dto.getObservacoes());
        }
    }

    private void atualizarValorTotalSeNecessario(Hospedagem hospedagem, HospedagemEntradaDTO dto) {
        if (hospedagem.getDataEntrada() != dto.getDataEntrada() || hospedagem.getDataSaida() != dto.getDataSaida()) {
            Double calculoHospedagem = calculoHospedagemService.calcular(hospedagem);
            hospedagem.setValorTotal(calculoHospedagem);
            Hospede hospede = hospedeService.findByDocumentoHospede(hospedagem.getHospede().getCpf());
            atualizarValoresHospede(hospede, calculoHospedagem);
        }
    }

    public void excluirHospedagem(Long id) {
        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedagem não encontrada com o ID: " + id));
        hospedagemRepository.delete(hospedagem);
    }
}
