package com.minicore.logistica.repository;

import java.time.LocalDate;
import java.util.List;

import com.minicore.logistica.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {

    List<Envio> findByFechaEnvioBetween(LocalDate inicio, LocalDate fin);
}
