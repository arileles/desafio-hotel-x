package com.hotel.x.hotel_x.domain.Hospede;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospedeRepository extends JpaRepository<Hospede, Long> {

    Optional<Hospede> findByCpf(String cpf);

    Optional<Hospede> findByNome(String nome);

    Optional<Hospede> findByTelefone(String telefone);

    boolean existsByCpf(String cpf);
}
