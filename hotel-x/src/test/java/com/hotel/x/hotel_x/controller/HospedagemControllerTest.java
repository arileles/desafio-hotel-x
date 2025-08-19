package com.hotel.x.hotel_x.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.x.hotel_x.domain.Hospedagem.Hospedagem;
import com.hotel.x.hotel_x.domain.Hospedagem.HospedagemRepository;
import com.hotel.x.hotel_x.domain.Hospedagem.HospedagemService;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemListarDTO;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HospedagemController.class)
class HospedagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HospedagemService hospedagemService;

    @MockitoBean
    private HospedagemRepository hospedagemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetHospedesAtivos() throws Exception {
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(1L);
        hospedagem.setDataEntrada(LocalDateTime.now());
        hospedagem.setAdicionalVeiculo(false);
        hospedagem.setHospede(hospede);

        Page<Hospedagem> page = new PageImpl<>(List.of(hospedagem));

        Mockito.when(hospedagemService.hospedesAtivos())
                .thenReturn(page);

        mockMvc.perform(get("/hospedagem/ativas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idHospedagem").value(1L))
                .andExpect(jsonPath("$.content[0].cpf").value("12345678901"));
    }

    @Test
    void testGetHospedesInativos() throws Exception {
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(2L);
        hospedagem.setDataEntrada(LocalDateTime.now().minusDays(2));
        hospedagem.setDataSaida(LocalDateTime.now());
        hospedagem.setAdicionalVeiculo(true);
        hospedagem.setHospede(hospede);
        Page<Hospedagem> page = new PageImpl<>(Collections.singletonList(hospedagem));
        Mockito.when(hospedagemService.hospedesInativos()).thenReturn(page);

        mockMvc.perform(get("/hospedagem/inativas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].idHospedagem").value(2L));
    }

    @Test
    void testGetHospedagemPorHospede() throws Exception {
        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(3L);
        hospedagem.setDataEntrada(LocalDateTime.now().minusDays(3));
        hospedagem.setAdicionalVeiculo(false);
        List<Hospedagem> hospedagens = Collections.singletonList(hospedagem);
        Mockito.when(hospedagemService.hospedesAtivos()).thenReturn(new PageImpl<>(hospedagens));

        mockMvc.perform(get("/hospedagem/12345678901"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostHospedagem() throws Exception {
        HospedagemEntradaDTO dto = new HospedagemEntradaDTO();
        dto.setHospede("12345678901");
        dto.setDataEntrada(LocalDateTime.now());
        dto.setAdicionalVeiculo(false);

        Hospedagem hospedagemSalva = new Hospedagem();
        hospedagemSalva.setId(4L);
        Mockito.when(hospedagemService.salvarHospedagem(any(HospedagemEntradaDTO.class))).thenReturn(hospedagemSalva);

        mockMvc.perform(post("/hospedagem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testPutHospedagemAtualizar() throws Exception {
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678900");
        hospede.setNome("Fulano de Tal");
        hospede.setTelefone("11999999999");

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setHospede(hospede);
        hospedagem.setId(5L);
        hospedagem.setDataEntrada(LocalDateTime.of(2025, 8, 19, 17, 31));
        hospedagem.setAdicionalVeiculo(true);
        hospedagem.setValorTotal(100.0);
        hospedagem.setObservacoes("Observações de teste");
        hospedagem.setDataSaida(LocalDateTime.of(2025, 8, 20, 17, 31));

        Mockito.when(hospedagemService.atualizarHospedagem(any(HospedagemEntradaDTO.class), eq(1L)))
                .thenReturn(new HospedagemListarDTO(hospedagem));
        String json = """
            {
                "hospede": "12345678900",
                "dataEntrada": "19/08/2025 17:31",
                "dataSaida": "20/08/2025 17:31",
                "adicionalVeiculo": true,
                "valorTotal": 100.0,
                "observacoes": "Observações de teste"
            }
            """;

        mockMvc.perform(put("/hospedagem/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
}
