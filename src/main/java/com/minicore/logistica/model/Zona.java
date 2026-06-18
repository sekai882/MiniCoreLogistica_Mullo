package com.minicore.logistica.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Integer idZona;

    @Column(name = "nombre_zona")
    private String nombreZona;

    @Column(name = "tarifa_por_kg")
    private BigDecimal tarifaPorKg;

    public Zona() {
    }

    // Getters y Setters

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public BigDecimal getTarifaPorKg() {
        return tarifaPorKg;
    }

    public void setTarifaPorKg(BigDecimal tarifaPorKg) {
        this.tarifaPorKg = tarifaPorKg;
    }
}
