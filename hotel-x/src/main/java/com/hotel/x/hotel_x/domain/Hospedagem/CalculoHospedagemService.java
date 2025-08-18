package com.hotel.x.hotel_x.domain.Hospedagem;

import com.hotel.x.hotel_x.domain.Hospedagem.dto.HospedagemEntradaDTO;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
public class CalculoHospedagemService {
    private static final double VALOR_DIA_UTIL = 120.0;
    private static final double VALOR_FINAL_SEMANA = 150.0;
    private static final double GARAGEM_DIA_UTIL = 15.0;
    private static final double GARAGEM_FINAL_SEMANA = 20.0;

    /**
     * Calcula o valor total de uma hospedagem.
     */
    public double calcular(Hospedagem hospedagem) {
        LocalDateTime entrada = hospedagem.getDataEntrada();
        LocalDateTime saida = hospedagem.getDataSaida();

        boolean usaGaragem = hospedagem.getAdicionalVeiculo();

        double total = 0.0;

        // Percorre cada dia entre entrada e saída
        LocalDateTime atual = entrada;
        while (!atual.toLocalDate().isAfter(saida.toLocalDate())) {
            DayOfWeek diaSemana = atual.getDayOfWeek();
            boolean fimDeSemana = (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY);

            // Valor da diária
            if (fimDeSemana) {
                total += VALOR_FINAL_SEMANA;
                if (usaGaragem) total += GARAGEM_FINAL_SEMANA;
            } else {
                total += VALOR_DIA_UTIL;
                if (usaGaragem) total += GARAGEM_DIA_UTIL;
            }

            atual = atual.plusDays(1);
        }

        // Regra do checkout após 16:30h → diária extra
        if (saida.toLocalTime().isAfter(java.time.LocalTime.of(16, 30))) {
            DayOfWeek diaSemana = saida.getDayOfWeek();
            boolean fimDeSemana = (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY);

            if (fimDeSemana) {
                total += VALOR_FINAL_SEMANA;
                if (usaGaragem) total += GARAGEM_FINAL_SEMANA;
            } else {
                total += VALOR_DIA_UTIL;
                if (usaGaragem) total += GARAGEM_DIA_UTIL;
            }
        }

        return total;
    }
}
