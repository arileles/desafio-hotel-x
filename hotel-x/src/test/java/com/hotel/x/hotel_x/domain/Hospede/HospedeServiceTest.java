package com.hotel.x.hotel_x.domain.Hospede;

import com.hotel.x.hotel_x.domain.Hospede.exceptions.HospedeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HospedeServiceTest {

    private HospedeRepository hospedeRepository;
    private HospedeService hospedeService;

    @BeforeEach
    void setUp() {
        hospedeRepository = Mockito.mock(HospedeRepository.class);
        hospedeService = new HospedeService();
        // Injeta o mock via reflexão
        try {
            var field = HospedeService.class.getDeclaredField("hospedeRepository");
            field.setAccessible(true);
            field.set(hospedeService, hospedeRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindByDocumentoHospedeSucesso() {
        Hospede hospede = new Hospede(1L, "João", "12345678901", "11999999999", 0.0, 0.0);
        Mockito.when(hospedeRepository.findByCpf("12345678901")).thenReturn(Optional.of(hospede));

        Hospede result = hospedeService.findByDocumentoHospede("12345678901");
        assertEquals("João", result.getNome());
    }

    @Test
    void testFindByDocumentoHospedeNaoEncontrado() {
        Mockito.when(hospedeRepository.findByCpf("000")).thenReturn(Optional.empty());
        assertThrows(HospedeNotFoundException.class, () -> hospedeService.findByDocumentoHospede("000"));
    }

    @Test
    void testFindByNomeHospedeSucesso() {
        Hospede hospede = new Hospede(1L, "Maria", "12345678901", "11988888888", 0.0, 0.0);
        Mockito.when(hospedeRepository.findByNome("Maria")).thenReturn(Optional.of(hospede));

        Hospede result = hospedeService.findByNomeHospede("Maria");
        assertEquals("12345678901", result.getCpf());
    }

    @Test
    void testFindByTelefoneHospedeSucesso() {
        Hospede hospede = new Hospede(1L, "Carlos", "98765432100", "11977777777", 0.0, 0.0);
        Mockito.when(hospedeRepository.findByTelefone("11977777777")).thenReturn(Optional.of(hospede));

        Hospede result = hospedeService.findByTelefoneHospede("11977777777");
        assertEquals("Carlos", result.getNome());
    }

    @Test
    void testAtualizarHospedeSucesso() {
        Hospede original = new Hospede(1L, "Ana", "12345678901", "11999999999", 0.0, 0.0);
        Hospede atualizado = new Hospede(1L, "Ana Paula", "12345678901", "11988888888", 0.0, 0.0);

        Mockito.when(hospedeRepository.findByCpf("12345678901")).thenReturn(Optional.of(original));
        Mockito.when(hospedeRepository.save(Mockito.any(Hospede.class))).thenReturn(atualizado);

        Hospede result = hospedeService.atualizarHospede(atualizado);
        assertEquals("Ana Paula", result.getNome());
        assertEquals("11988888888", result.getTelefone());
    }

    @Test
    void testAtualizarHospedeTelefoneInvalido() {
        Hospede original = new Hospede(1L, "Ana", "12345678901", "11999999999", 0.0, 0.0);
        Hospede atualizado = new Hospede(1L, "Ana", "12345678901", "telefone_invalido", 0.0, 0.0);

        Mockito.when(hospedeRepository.findByCpf("12345678901")).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class, () -> hospedeService.atualizarHospede(atualizado));
    }

    @Test
    void testVerificacaoHospedeCpfJaCadastrado() {
        Hospede hospede = new Hospede(null, "João", "12345678901", "11999999999", 0.0, 0.0);
        Mockito.when(hospedeRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> hospedeService.verificacaoHospede(hospede));
    }

    @Test
    void testVerificacaoHospedeSucesso() {
        Hospede hospede = new Hospede(null, "João", "12345678901", "11999999999", 0.0, 0.0);
        Mockito.when(hospedeRepository.existsByCpf("12345678901")).thenReturn(false);
        Mockito.when(hospedeRepository.save(hospede)).thenReturn(hospede);

        Hospede result = hospedeService.verificacaoHospede(hospede);
        assertEquals("João", result.getNome());
    }

}