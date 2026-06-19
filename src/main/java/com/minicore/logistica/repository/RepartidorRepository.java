package com.minicore.logistica.repository;

import com.minicore.logistica.model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Integer> {
}
