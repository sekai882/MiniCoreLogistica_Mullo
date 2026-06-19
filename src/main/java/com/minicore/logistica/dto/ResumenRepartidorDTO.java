package com.minicore.logistica.dto;

import java.math.BigDecimal;

public class ResumenRepartidorDTO {

    private String nombreRepartidor;
    private long cantidadEnvios;
    private BigDecimal totalKg;
    private String zonas;
    private String tarifas;
    private BigDecimal costoTotal;

    public ResumenRepartidorDTO(String nombreRepartidor, long cantidadEnvios, BigDecimal totalKg,
                                String zonas, String tarifas, BigDecimal costoTotal) {
        this.nombreRepartidor = nombreRepartidor;
        this.cantidadEnvios = cantidadEnvios;
        this.totalKg = totalKg;
        this.zonas = zonas;
        this.tarifas = tarifas;
        this.costoTotal = costoTotal;
    }

    // Getters (inmutable para la vista)

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    public long getCantidadEnvios() {
        return cantidadEnvios;
    }

    public BigDecimal getTotalKg() {
        return totalKg;
    }

    public String getZonas() {
        return zonas;
    }

    public String getTarifas() {
        return tarifas;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }
}
