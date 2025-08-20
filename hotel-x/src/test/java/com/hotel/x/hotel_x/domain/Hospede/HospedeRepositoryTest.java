package com.hotel.x.hotel_x.domain.Hospede;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HospedeRepositoryTest {

    @Autowired
    private HospedeRepository hospedeRepository;

    @Test
    @DisplayName("Deve salvar e buscar hóspede por CPF")
    void testSaveAndFindByCpf() {
        Hospede hospede = new Hospede(null, "João Silva", "445.279.210-37", "11999999999", 0.0, 0.0);
        hospede = hospedeRepository.save(hospede);

        Optional<Hospede> encontrado = hospedeRepository.findByCpf("445.279.210-37");
        assertTrue(encontrado.isPresent());
        assertEquals("João Silva", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve buscar hóspede por nome")
    void testFindByNome() {
        Hospede hospede = new Hospede(null, "Maria Souza", "123.456.789-09", "11988888888", 0.0, 0.0);
        hospedeRepository.save(hospede);

        Optional<Hospede> encontrado = hospedeRepository.findByNome("Maria Souza");
        assertTrue(encontrado.isPresent());
        assertEquals("123.456.789-09", encontrado.get().getCpf());
    }

    @Test
    @DisplayName("Deve buscar hóspede por telefone")
    void testFindByTelefone() {
        Hospede hospede = new Hospede(null, "Carlos Lima", "987.654.321-00", "11977777777", 0.0, 0.0);
        hospedeRepository.save(hospede);

        Optional<Hospede> encontrado = hospedeRepository.findByTelefone("11977777777");
        assertTrue(encontrado.isPresent());
        assertEquals("Carlos Lima", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Não deve permitir salvar hóspede com CPF inválido")
    void testNaoPermiteSalvarHospedeCpfInvalido() {
        Hospede hospede = new Hospede(null, "Teste CPF", "445.279.210-1", "11999999999", 0.0, 0.0);

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            hospedeRepository.saveAndFlush(hospede);
        });
    }

    @Test
    @DisplayName("Não deve permitir salvar hóspede com telefone inválido")
    void testNaoPermiteSalvarHospedeTelefoneInvalido() {
        Hospede hospede = new Hospede(null, "Teste Telefone", "445.279.210-37", "telefone_invalido", 0.0, 0.0);

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            hospedeRepository.saveAndFlush(hospede);
        });
    }
}