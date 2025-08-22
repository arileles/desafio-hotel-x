package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospede.Hospede;
import com.hotel.x.hotel_x.domain.Hospede.HospedeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HospedagemRepositoryTest {

    @Autowired
    private HospedagemRepository hospedagemRepository;

    @Autowired
    private HospedeRepository hospedeRepository;

    @Test
    @DisplayName("Deve encontrar hospedagens ativas (data_saida nula)")
    void testFindByDataSaidaIsNull() {
        Hospede hospede = new Hospede(null, "Teste", "445.279.210-37", "11999999999", 0.0, 0.0);
        hospede = hospedeRepository.save(hospede);

        Hospedagem hospedagem = new Hospedagem(null, hospede, LocalDateTime.now(), null, false, 100.0, "Ativa");
        hospedagemRepository.save(hospedagem);

        var page = hospedagemRepository.findByDataSaidaIsNull(PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("Deve encontrar hospedagens inativas (data_saida não nula)")
    void testFindByDataSaidaIsNotNull() {
        Hospede hospede = new Hospede(null, "Teste2", "445.279.210-37", "11988888888", 0.0, 0.0);
        hospede = hospedeRepository.save(hospede);

        Hospedagem hospedagem = new Hospedagem(null, hospede, LocalDateTime.now().minusDays(2), LocalDateTime.now(), false, 200.0, "Inativa");
        hospedagemRepository.save(hospedagem);

        var page = hospedagemRepository.findByDataSaidaIsNotNull(PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar hospedagens pelo CPF do hóspede")
    void testFindAllByHospedeCpf() {
        Hospede hospede = new Hospede(null, "Teste3", "445.279.210-37", "11977777777", 0.0, 0.0);
        hospede = hospedeRepository.save(hospede);

        Hospedagem hospedagem = new Hospedagem(null, hospede, LocalDateTime.now(), null, true, 300.0, "Busca CPF");
        hospedagemRepository.save(hospedagem);

        List<Hospedagem> result = hospedagemRepository.findAllByHospede_Cpf(hospede.getCpf());
        assertFalse(result.isEmpty());
        assertEquals("Busca CPF", result.get(0).getObservacoes());
    }

    @Test
    @DisplayName("Deve buscar hospedagens pelo CPF do hóspede, porém o CPF cadastrado não segue o padrão brasileiro")
    void testFindAllByHospedeCpfErrado() {
        Hospede hospede = new Hospede(null, "Teste3", "445.279.210-1", "11977777777", 0.0, 0.0);
        Hospedagem hospedagem = new Hospedagem(null, hospede, LocalDateTime.now(), null, true, 300.0, "Busca CPF");

        List<Hospedagem> result = hospedagemRepository.findAllByHospede_Cpf(hospede.getCpf());
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            hospedeRepository.saveAndFlush(
                hospede
            );
        });
    }

    @Test
        @DisplayName("Deve buscar a última hospedagem pelo CPF do hóspede")
        void testFindFirstByHospedeCpfOrderByDataSaidaDesc() {
            Hospede hospede = new Hospede(null, "Teste4", "147.020.120-88", "11966666666", 0.0, 0.0);
            hospede = hospedeRepository.save(hospede);

            Hospedagem hospedagem1 = new Hospedagem(null, hospede, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3), false, 150.0, "Primeira");
            Hospedagem hospedagem2 = new Hospedagem(null, hospede, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), false, 250.0, "Última");
            hospedagemRepository.save(hospedagem1);
            hospedagemRepository.save(hospedagem2);

            Hospedagem result = hospedagemRepository.findFirstByHospede_CpfOrderByDataSaidaDesc(hospede.getCpf());
            assertNotNull(result);
            assertEquals("Última", result.getObservacoes());
        }
}
