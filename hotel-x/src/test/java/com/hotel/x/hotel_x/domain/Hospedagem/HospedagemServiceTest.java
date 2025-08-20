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
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        HospedagemEntradaDTO dto = HospedagemEntradaDTO.builder()
                .dataEntrada(LocalDateTime.now())
                .adicionalVeiculo(false)
                .hospede(hospede.getCpf()).build();

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
        HospedagemEntradaDTO dto = HospedagemEntradaDTO.builder()
                .dataEntrada(LocalDateTime.now().minusDays(1))
                .dataSaida(LocalDateTime.now())
                .adicionalVeiculo(false)
                .build();

        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        Hospedagem hospedagem = Hospedagem.builder().
                id(1L)
                .dataEntrada(dto.getDataEntrada())
                .hospede(hospede)
                .adicionalVeiculo(dto.isAdicionalVeiculo())
                .build();


        Mockito.when(hospedagemRepository.findById(1L)).thenReturn(java.util.Optional.of(hospedagem));
        Mockito.when(hospedagemRepository.save(any(Hospedagem.class))).thenReturn(hospedagem);

        HospedagemListarDTO result = hospedagemService.atualizarHospedagem(dto, 1L);
        assertNotNull(result);
        assertEquals(1L, result.idHospedagem());
    }

    @Test
    void testHospedesAtivos() {
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        Hospedagem hospedagem = Hospedagem.builder().
                id(1L)
                .dataEntrada(LocalDateTime.now().minusDays(1))
                .hospede(hospede)
                .adicionalVeiculo(false)
                .build();

        Page<Hospedagem> page = new PageImpl<>(Collections.singletonList(hospedagem));
        Mockito.when(hospedagemRepository.findByDataSaidaIsNull(any(PageRequest.class))).thenReturn(page);

        Page<Hospedagem> result = hospedagemService.hospedesAtivos();
        assertNotNull(result);
        assertEquals(1, result.getTotalElements(), "Deve retornar 1 para hóspedes ativos");
    }

    @Test
    void testConferirHospedagemDataSaidaFutura() {
        HospedagemEntradaDTO dto = HospedagemEntradaDTO.builder().dataSaida(LocalDateTime.now().plusDays(1)).build();
        assertThrows(RuntimeException.class, () -> hospedagemService.conferirHospedagem(dto));
    }

    @Test
    void testConferirHospedagemDataEntradaFutura() {
        HospedagemEntradaDTO dto = HospedagemEntradaDTO.builder().dataSaida(LocalDateTime.now().plusDays(1)).build();
        assertThrows(RuntimeException.class, () -> hospedagemService.conferirHospedagem(dto));
    }

    @Test
    void testAtualizarValorTotalSeNecessarioQuandoDatasMudam() throws Exception {
        HospedagemEntradaDTO dto = HospedagemEntradaDTO.builder()
                .dataEntrada(LocalDateTime.now().minusDays(5))
                .dataEntrada(LocalDateTime.now().minusDays(1)).build();

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setDataEntrada(LocalDateTime.now().minusDays(10));
        hospedagem.setDataSaida(LocalDateTime.now().minusDays(7));

        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        hospedagem.setHospede(hospede);
        dto.setHospede(hospede.getCpf());

        Mockito.when(calculoHospedagemService.calcular(any(Hospedagem.class))).thenReturn(300.0);
        Mockito.when(hospedeService.findByDocumentoHospede((hospede.getCpf()))).thenReturn(hospede);

        Method method = HospedagemService.class.getDeclaredMethod("atualizarValorTotalSeNecessario", Hospedagem.class, HospedagemEntradaDTO.class);
        method.setAccessible(true);
        method.invoke(hospedagemService, hospedagem, dto);

        verify(calculoHospedagemService, times(1)).calcular(hospedagem);
        assertEquals(300.0, hospedagem.getValorTotal());
    }

    @Test
    void testHospedesInativos() {
        Hospede hospede = new Hospede();
        hospede.setCpf("12345678901");

        Hospedagem hospedagem = Hospedagem.builder().
                id(1L)
                .dataEntrada(LocalDateTime.now().minusDays(1))
                .dataSaida(LocalDateTime.now())
                .hospede(hospede)
                .adicionalVeiculo(false)
                .build();

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