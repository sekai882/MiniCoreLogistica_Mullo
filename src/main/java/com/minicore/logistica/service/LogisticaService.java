package com.minicore.logistica.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.minicore.logistica.dto.ResumenRepartidorDTO;
import com.minicore.logistica.model.Envio;
import com.minicore.logistica.model.Repartidor;
import com.minicore.logistica.repository.EnvioRepository;
import com.minicore.logistica.repository.RepartidorRepository;
import org.springframework.stereotype.Service;

@Service
public class LogisticaService {

    private final RepartidorRepository repartidorRepository;
    private final EnvioRepository envioRepository;

    public LogisticaService(RepartidorRepository repartidorRepository, EnvioRepository envioRepository) {
        this.repartidorRepository = repartidorRepository;
        this.envioRepository = envioRepository;
    }

    public List<ResumenRepartidorDTO> obtenerReporteCostos(LocalDate fechaInicio, LocalDate fechaFin) {

        List<Repartidor> repartidores = repartidorRepository.findAll();
        List<Envio> enviosFiltrados = envioRepository.findByFechaEnvioBetween(fechaInicio, fechaFin);

        // Agrupar envíos por ID de repartidor
        Map<Integer, List<Envio>> enviosPorRepartidor = enviosFiltrados.stream()
                .collect(Collectors.groupingBy(envio -> envio.getRepartidor().getIdRepartidor()));

        List<ResumenRepartidorDTO> reporte = new ArrayList<>();

        for (Repartidor repartidor : repartidores) {

            List<Envio> enviosDelRepartidor = enviosPorRepartidor.get(repartidor.getIdRepartidor());

            if (enviosDelRepartidor == null || enviosDelRepartidor.isEmpty()) {
                // Repartidor sin envíos en el periodo
                reporte.add(new ResumenRepartidorDTO(
                        repartidor.getNombre(),
                        0,
                        BigDecimal.ZERO,
                        "—",
                        "—",
                        BigDecimal.ZERO
                ));
            } else {
                // Cantidad total de envíos
                long cantidadEnvios = enviosDelRepartidor.size();

                // Sumatoria total de kilogramos transportados
                BigDecimal totalKg = enviosDelRepartidor.stream()
                        .map(Envio::getPesoKg)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Zonas únicas visitadas
                String zonas = enviosDelRepartidor.stream()
                        .map(envio -> envio.getZona().getNombreZona())
                        .distinct()
                        .collect(Collectors.joining(", "));

                // Tarifas únicas aplicadas
                String tarifas = enviosDelRepartidor.stream()
                        .map(envio -> envio.getZona().getTarifaPorKg().toString())
                        .distinct()
                        .collect(Collectors.joining(", "));

                // COSTO TOTAL: pesoKg * tarifaPorKg por cada envío
                BigDecimal costoTotal = enviosDelRepartidor.stream()
                        .map(envio -> envio.getPesoKg().multiply(envio.getZona().getTarifaPorKg()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                reporte.add(new ResumenRepartidorDTO(
                        repartidor.getNombre(),
                        cantidadEnvios,
                        totalKg,
                        zonas,
                        tarifas,
                        costoTotal
                ));
            }
        }

        return reporte;
    }
}
