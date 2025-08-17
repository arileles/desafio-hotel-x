package com.hotel.x.hotel_x.domain.Hospedagem;


import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {

    Page<Hospedagem> findByDataSaidaIsNotNull(Pageable pageable);

    Page<Hospedagem> findByDataSaidaIsNull(PageRequest of);
}
