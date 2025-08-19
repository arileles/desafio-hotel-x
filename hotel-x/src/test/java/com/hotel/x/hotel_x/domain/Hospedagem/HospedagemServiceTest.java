package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.controller.HospedeController;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemListarDTO;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.HospedeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HospedagemServiceTest {

    @Mock
    private HospedagemRepository hospedagemRepository;

    @InjectMocks
    private HospedagemService hospedagemService;
    @InjectMocks
    private HospedeRepository hospedeRepository;

    @BeforeEach
    void setUp() throws Exception {
        hospedagemRepository = Mockito.mock(HospedagemRepository.class);
        hospedeRepository = Mockito.mock(HospedeRepository.class); // Adicionado mock do hospedeRepository
        hospedagemService = new HospedagemService();
        // Se hospedagemRepository for privado, use reflexão para setar
        Field repoField = HospedagemService.class.getDeclaredField("hospedagemRepository");
        repoField.setAccessible(true);
        repoField.set(hospedagemService, hospedagemRepository);
        Field repo1Field = HospedagemService.class.getDeclaredField("hospedeRepository");
        repo1Field.setAccessible(true);
        repo1Field.set(hospedagemService, hospedeRepository);
    }

    @Test
    void testSalvarHospedagem() {
        HospedagemEntradaDTO dto = new HospedagemEntradaDTO();
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");
        dto.setHospede(hospede.getCpf());
        dto.setDataEntrada(LocalDateTime.now());
        dto.setAdicionalVeiculo(false);

        Hospedagem hospedagemSalva = new Hospedagem();
        hospedagemSalva.setId(1L);
        hospedagemSalva.setHospede(hospede);

        Mockito.when(hospedeRepository.findByCpf(hospede.getCpf())).thenReturn(Optional.of(hospede));
        Mockito.when(hospedagemRepository.save(Mockito.any(Hospedagem.class)))
                .thenAnswer(invocation -> {
                    Hospedagem arg = invocation.getArgument(0);
                    arg.setId(1L); // simula o ID gerado pelo banco
                    return arg;
                });

        Hospedagem result = hospedagemService.salvarHospedagem(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("12345678901", result.getHospede().getCpf());
    }

    @Test
    void testAtualizarHospedagem() {
        HospedagemEntradaDTO dto = new HospedagemEntradaDTO();
        dto.setDataEntrada(LocalDateTime.now().minusDays(1));
        dto.setDataSaida(LocalDateTime.now());
        dto.setAdicionalVeiculo(false);

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(1L);
        hospedagem.setDataEntrada(dto.getDataEntrada());
        hospedagem.setAdicionalVeiculo(dto.isAdicionalVeiculo());

        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");
        hospedagem.setHospede(hospede);

        Mockito.when(hospedagemRepository.findById(1L)).thenReturn(java.util.Optional.of(hospedagem));
        Mockito.when(hospedagemRepository.save(any(Hospedagem.class))).thenReturn(hospedagem);

        HospedagemListarDTO result = hospedagemService.atualizarHospedagem(dto, 1L);
        assertNotNull(result);
        assertEquals(1L, result.idHospedagem());
    }

    @Test
    void testHospedesAtivos() {
        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(1L);
        hospedagem.setDataEntrada(LocalDateTime.now().minusDays(1));
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");
        hospedagem.setHospede(hospede);
        hospedagem.setAdicionalVeiculo(false);
        Page<Hospedagem> page = new PageImpl<>(Collections.singletonList(hospedagem));
        Mockito.when(hospedagemRepository.findByDataSaidaIsNull(any(PageRequest.class))).thenReturn(page);

        Page<Hospedagem> result = hospedagemService.hospedesAtivos();
        assertNotNull(result);
        assertEquals(1, result.getTotalElements(), "Deve retornar 1 para hóspedes ativos");
        }

    @Test
    void testHospedesInativos() {
        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(1L);
        hospedagem.setDataEntrada(LocalDateTime.now().minusDays(1));
        hospedagem.setDataSaida(LocalDateTime.now());
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");
        hospedagem.setHospede(hospede);
        hospedagem.setAdicionalVeiculo(false);

        Page<Hospedagem> page = new PageImpl<>(Collections.singletonList(hospedagem));
        Mockito.when(hospedagemRepository.findByDataSaidaIsNotNull(any(PageRequest.class))).thenReturn(page);

        Page<Hospedagem> result = hospedagemService.hospedesInativos();
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }


    @Test
    void testExcluirHospedagemSucesso() {
        Long id = 1L;
        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setId(id);

        Mockito.when(hospedagemRepository.findById(id)).thenReturn(Optional.of(hospedagem));

        hospedagemService.excluirHospedagem(id);

        verify(hospedagemRepository, times(1)).findById(id);
        verify(hospedagemRepository, times(1)).delete(hospedagem);
    }

    @Test
    void testExcluirHospedagemNaoEncontrada() {
        Long id = 1L;

        Mockito.when(hospedagemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hospedagemService.excluirHospedagem(id));

        verify(hospedagemRepository, times(1)).findById(id);
        verify(hospedagemRepository, never()).delete(any());
    }
}