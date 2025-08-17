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


@RestController
@RequestMapping("hospedagem")
public class HospedagemController {

    @Autowired
    private HospedagemService hospedagemService;
    @Autowired
    private HospedagemRepository hospedagemRepository;

    @GetMapping("/ativas")
    public ResponseEntity<Page<HospedagemListarDTO>> getHospedesAtivos(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<Hospedagem> hospedesAtivos = hospedagemService.hospedesAtivos();
        Page<HospedagemListarDTO> dto =  hospedesAtivos
                .map(HospedagemListarDTO::new);
        return ResponseEntity.ok(dto);
    }
    @PostMapping
    public ResponseEntity<HospedagemListarDTO> postHospedagem(@RequestBody HospedagemEntradaDTO hospedagemEntradaDTO, UriComponentsBuilder uriComponentsBuilder){
        Hospedagem hospedagemSalva = hospedagemService.salvarHospedagem(hospedagemEntradaDTO);
        URI uri = uriComponentsBuilder.path("hospede/{id}").buildAndExpand(hospedagemSalva.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @GetMapping("/inativas")
    public ResponseEntity<Page<HospedagemListarDTO>> getHospedesInativos(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<Hospedagem> hospedesInativos = hospedagemService.hospedesInativos();
        Page<HospedagemListarDTO> dto =  hospedesInativos
                .map(HospedagemListarDTO::new);
        return ResponseEntity.ok(dto);
    }



}
