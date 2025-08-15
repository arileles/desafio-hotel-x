package com.hotel.x.hotel_x.controller;

import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.HospedeRepository;
import com.hotel.x.hotel_x.domain.Hospede.HospedeService;
import com.hotel.x.hotel_x.domain.Hospede.dto.HospedeListagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("hospede")
public class HospedeController {
    @Autowired
    private HospedeRepository hospedeRepository;
    @Autowired
    private HospedeService hospedeService;
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Optional<Object>> listHospedeByDocumento(@PathVariable String cpf) {
        Hospede hospede = hospedeService.findByDocumentoHospede(cpf);
        return ResponseEntity.ok(Optional.of(new HospedeListagem(hospede)));
    }
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Optional<Object>> listHospedeByNome(@PathVariable String nome) {
        Hospede hospede = hospedeService.findByNomeHospede(nome);
        return ResponseEntity.ok(Optional.of(new HospedeListagem(hospede)));
    }
    @GetMapping("/telefone/{telefone}")
    public ResponseEntity<Optional<Object>> listHospedeByTelefone(@PathVariable String telefone) {
        Hospede hospede = hospedeService.findByTelefoneHospede(telefone);
        return ResponseEntity.ok(Optional.of(new HospedeListagem(hospede)));
    }
    @GetMapping()
    public ResponseEntity<Page<HospedeListagem>> listAllHospedes( @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        var hospedes  = hospedeRepository.findAll(pageable);
        Page<HospedeListagem> dto = hospedes.map(HospedeListagem::new);
        return ResponseEntity.ok(dto);
    }
    @PostMapping
    public ResponseEntity<HospedeListagem> postHospede(@RequestBody Hospede hospede, UriComponentsBuilder uriComponentsBuilder){
        Hospede salvo = hospedeService.verificacaoCpf(hospede);
        URI uri = uriComponentsBuilder.path("hospede/{id}").buildAndExpand(salvo.getId()).toUri();
        return  ResponseEntity.created(uri).build();
    }
    @PutMapping
    public ResponseEntity<HospedeListagem> putHospede(@RequestBody Hospede hospede){
        Hospede hospedeEncontrado = hospedeService.atualizarHospede(hospede);
        return ResponseEntity.ok(new HospedeListagem(hospedeEncontrado));
    }
}
