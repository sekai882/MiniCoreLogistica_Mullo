package com.minicore.logistica.controller;

import java.time.LocalDate;
import java.util.List;

import com.minicore.logistica.dto.ResumenRepartidorDTO;
import com.minicore.logistica.service.LogisticaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(value = "fechaInicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        if (fechaInicio != null && fechaFin != null) {
            List<ResumenRepartidorDTO> reporte = logisticaService.obtenerReporteCostos(fechaInicio, fechaFin);
            model.addAttribute("reporte", reporte);
        } else {
            model.addAttribute("reporte", null);
        }

        return "index";
    }
}
