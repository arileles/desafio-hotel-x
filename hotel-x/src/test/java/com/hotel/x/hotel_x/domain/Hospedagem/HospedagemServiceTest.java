package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemListarDTO;
import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.HospedeRepository;
import com.hotel.x.hotel_x.domain.Hospede.HospedeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospedagemServiceTest {

    @Mock
    private HospedagemRepository hospedagemRepository;

    @InjectMocks
    private HospedagemService hospedagemService;

    @Mock
    private HospedeService hospedeService;

    @Mock
    private CalculoHospedagemService calculoHospedagemService;

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

        Mockito.when(hospedeService.findByDocumentoHospede(hospede.getCpf())).thenReturn(hospede);
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
    void testConferirHospedagemDataSaidaFutura() {
        HospedagemEntradaDTO dto = new HospedagemEntradaDTO();
        dto.setDataSaida(LocalDateTime.now().plusDays(1));

        assertThrows(RuntimeException.class, () -> hospedagemService.conferirHospedagem(dto));
    }

    @Test
    void testConferirHospedagemDataEntradaFutura() {
        HospedagemEntradaDTO dto = new HospedagemEntradaDTO();
        dto.setDataEntrada(LocalDateTime.now().plusDays(1));

        assertThrows(RuntimeException.class, () -> hospedagemService.conferirHospedagem(dto));
    }

    @Test
    void testAtualizarValorTotalSeNecessarioQuandoDatasMudam() throws Exception {
        HospedagemEntradaDTO dto = new HospedagemEntradaDTO();
        dto.setDataEntrada(LocalDateTime.now().minusDays(5));
        dto.setDataSaida(LocalDateTime.now().minusDays(1));

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setDataEntrada(LocalDateTime.now().minusDays(10));
        hospedagem.setDataSaida(LocalDateTime.now().minusDays(7));

        Mockito.when(calculoHospedagemService.calcular(any(Hospedagem.class))).thenReturn(300.0);

        Method method = HospedagemService.class.getDeclaredMethod("atualizarValorTotalSeNecessario", Hospedagem.class, HospedagemEntradaDTO.class);
        method.setAccessible(true);
        method.invoke(hospedagemService, hospedagem, dto);

        verify(calculoHospedagemService, times(1)).calcular(hospedagem);
        assertEquals(300.0, hospedagem.getValorTotal());
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