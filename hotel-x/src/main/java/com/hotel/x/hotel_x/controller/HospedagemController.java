package com.hotel.x.hotel_x.controller;

import com.hotel.x.hotel_x.domain.Hospedagem.Hospedagem;
import com.hotel.x.hotel_x.domain.Hospedagem.HospedagemRepository;
import com.hotel.x.hotel_x.domain.Hospedagem.HospedagemService;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemListarDTO;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

@RestController
@RequestMapping("hospedagem")
public class HospedagemController {

    @Autowired
    private HospedagemService hospedagemService;

    @Autowired
    private HospedagemRepository hospedagemRepository;

    @GetMapping("/ativas")
    public ResponseEntity<Page<HospedagemListarDTO>> buscarHospedesAtivos(@PageableDefault(size = 100, sort = "nome") Pageable pageable) {
        Page<Hospedagem> hospedesAtivos = hospedagemService.hospedesAtivos();
        Page<HospedagemListarDTO> dto =  hospedesAtivos
                .map(HospedagemListarDTO::new);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<HospedagemListarDTO> criarHospedagem(@RequestBody HospedagemEntradaDTO hospedagemEntradaDTO, UriComponentsBuilder uriComponentsBuilder){
        Hospedagem hospedagemSalva = hospedagemService.salvarHospedagem(hospedagemEntradaDTO);
        URI uri = uriComponentsBuilder.path("hospede/{id}").buildAndExpand(hospedagemSalva.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/inativas")
    public ResponseEntity<Page<HospedagemListarDTO>> buscarHospedesInativos(@PageableDefault(size = 100, sort = "nome") Pageable pageable) {
        Page<Hospedagem> hospedesInativos = hospedagemService.hospedesInativos();
        Page<HospedagemListarDTO> dto =  hospedesInativos
                .map(HospedagemListarDTO::new);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Stream<Object>> buscarHospedagemPorHospede(@PathVariable String cpf){
        Stream<Object> dto =  hospedagemRepository.findAllByHospede_Cpf(cpf).stream().map(HospedagemListarDTO::new);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospedagemListarDTO> atualizarHospedagem(@PathVariable Long id, @RequestBody HospedagemEntradaDTO hospedagemEntradaDTO){
        return ResponseEntity.ok(hospedagemService.atualizarHospedagem(hospedagemEntradaDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHospedagem(@PathVariable Long id) {
        hospedagemService.excluirHospedagem(id);
        return ResponseEntity.noContent().build();
    }
}
