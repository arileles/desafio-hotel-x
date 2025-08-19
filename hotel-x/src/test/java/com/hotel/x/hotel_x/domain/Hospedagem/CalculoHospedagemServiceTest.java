package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospedagem.CalculoHospedagemService;
import com.hotel.x.hotel_x.domain.Hospedagem.Hospedagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculoHospedagemServiceTest {

    private CalculoHospedagemService service;

    @BeforeEach
    void setUp() {
        service = new CalculoHospedagemService();
    }

    @Test
    void calculaDiariaDiaUtilSemGaragem() {
        Hospedagem hospedagem = Mockito.mock(Hospedagem.class);
        Mockito.when(hospedagem.getDataEntrada()).thenReturn(LocalDateTime.of(2024, 6, 3, 14, 0)); // Segunda
        Mockito.when(hospedagem.getDataSaida()).thenReturn(LocalDateTime.of(2024, 6, 4, 10, 0));   // Terça
        Mockito.when(hospedagem.getAdicionalVeiculo()).thenReturn(false);

        double valor = service.calcular(hospedagem);
        assertEquals(240.0, valor); // 2 dias úteis x 120
    }

    @Test
    void calculaDiariaFinalSemanaComGaragem() {
        Hospedagem hospedagem = Mockito.mock(Hospedagem.class);
        Mockito.when(hospedagem.getDataEntrada()).thenReturn(LocalDateTime.of(2024, 6, 8, 14, 0)); // Sábado
        Mockito.when(hospedagem.getDataSaida()).thenReturn(LocalDateTime.of(2024, 6, 9, 10, 0));   // Domingo
        Mockito.when(hospedagem.getAdicionalVeiculo()).thenReturn(true);

        double valor = service.calcular(hospedagem);
        assertEquals(340.0, valor); // (150+20) x 2 dias
    }

    @Test
    void calculaDiariaExtraCheckoutApos1630() {
        Hospedagem hospedagem = Mockito.mock(Hospedagem.class);
        Mockito.when(hospedagem.getDataEntrada()).thenReturn(LocalDateTime.of(2024, 6, 3, 14, 0)); // Segunda
        Mockito.when(hospedagem.getDataSaida()).thenReturn(LocalDateTime.of(2024, 6, 4, 17, 0));   // Terça, após 16:30
        Mockito.when(hospedagem.getAdicionalVeiculo()).thenReturn(false);

        double valor = service.calcular(hospedagem);
        assertEquals(360.0, valor); // 2 dias úteis + 1 diária extra
    }

    @Test
    void calculaDiariaComGaragemDiaUtilEFinalSemana() {
        Hospedagem hospedagem = Mockito.mock(Hospedagem.class);
        Mockito.when(hospedagem.getDataEntrada()).thenReturn(LocalDateTime.of(2024, 6, 7, 14, 0)); // Sexta
        Mockito.when(hospedagem.getDataSaida()).thenReturn(LocalDateTime.of(2024, 6, 9, 10, 0));   // Domingo
        Mockito.when(hospedagem.getAdicionalVeiculo()).thenReturn(true);

        double valor = service.calcular(hospedagem);
        // Sexta (120+15), Sábado (150+20), Domingo (150+20)
        assertEquals(475.0, valor);
    }
}

