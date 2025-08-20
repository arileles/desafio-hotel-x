package com.hotel.x.hotel_x.controller;

import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.HospedeRepository;
import com.hotel.x.hotel_x.domain.Hospede.HospedeService;
import com.hotel.x.hotel_x.domain.Hospede.dto.HospedeEntradaDTO;
import com.hotel.x.hotel_x.domain.Hospede.dto.HospedeListagemDTO;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(HospedeController.class)
class HospedeControllerTest {

    @MockitoBean
    private HospedeRepository hospedeRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HospedeService hospedeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void buscarHospedePorDocumento_deveRetornarHospede() throws Exception {
        Hospede hospede = new Hospede();
        hospede.setId(1L);
        hospede.setNome("Ariel");

        when(hospedeService.findByDocumentoHospede("123")).thenReturn(hospede);

        mockMvc.perform(get("/hospede/cpf/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ariel"));

        verify(hospedeService).findByDocumentoHospede("123");
    }

    @Test
    void buscarTodosOsHospedes_deveRetornarPagina() throws Exception {
        HospedeListagemDTO h1 = HospedeListagemDTO.builder()
                .cpf("12345678901")
                .telefone("11987654321")
                .nome("Ariel").build();

        Page<HospedeListagemDTO> page = new PageImpl<>(List.of(h1));
        when(hospedeService.buscarTodosOsHospedes(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/hospede"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Ariel"));
    }

    @Test
    void criarHospede_deveRetornarCreated() throws Exception {
        Hospede salvo = new Hospede();
        salvo.setId(1L);
        salvo.setNome("Ariel");

        when(hospedeService.verificarHospede(any(HospedeEntradaDTO.class))).thenReturn(salvo);

        mockMvc.perform(post("/hospede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salvo)))
                .andExpect(status().isCreated());
    }

    @Test
    void atualizarHospede_deveRetornarOk() throws Exception {
        Hospede hospede = new Hospede();
        hospede.setId(1L);
        hospede.setNome("Ariel");

        when(hospedeService.atualizarHospede(any(Hospede.class))).thenReturn(hospede);

        mockMvc.perform(put("/hospede")
                        .contentType("application/json")
                        .content("{\"id\":1,\"nome\":\"Ariel\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ariel"));
    }

    @Test
    void deletarHospede_deveRetornarNoContent() throws Exception {
        Long id = 1L;

        doNothing().when(hospedeService).deletarHospede(id);

        mockMvc.perform(delete("/hospede/{id}", id))
                .andExpect(status().isNoContent());

        verify(hospedeService).deletarHospede(id);
    }
}