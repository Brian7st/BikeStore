package com.bikeStore.demo.controller;

import com.bikeStore.demo.service.FacturaPdfService;
import com.bikeStore.demo.service.ReporteExportService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReporteController {

    private final ReporteExportService reporteExportService;
    private final FacturaPdfService    facturaPdfService;

    private static final MediaType EXCEL_MEDIA_TYPE = MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    // ======================== VENTAS ========================

    @GetMapping("/ventas/export/pdf")
    public ResponseEntity<byte[]> exportarVentasPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        validarRangoFechas(fechaInicio, fechaFin);

        byte[] pdf = reporteExportService.generarPdfVentas(
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(23, 59, 59)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-ventas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/ventas/export/excel")
    public ResponseEntity<byte[]> exportarVentasExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        validarRangoFechas(fechaInicio, fechaFin);

        byte[] excel = reporteExportService.generarExcelVentas(
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(23, 59, 59)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-ventas.xlsx")
                .contentType(EXCEL_MEDIA_TYPE)
                .body(excel);
    }

    // ======================== INVENTARIO ========================

    @GetMapping("/inventario/export/pdf")
    public ResponseEntity<byte[]> exportarInventarioPdf() {
        byte[] pdf = reporteExportService.generarPdfInventario();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-inventario.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/inventario/export/excel")
    public ResponseEntity<byte[]> exportarInventarioExcel() {
        byte[] excel = reporteExportService.generarExcelInventario();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-inventario.xlsx")
                .contentType(EXCEL_MEDIA_TYPE)
                .body(excel);
    }

    // ======================== MOVIMIENTOS ========================

    @GetMapping("/movimientos/export/pdf")
    public ResponseEntity<byte[]> exportarMovimientosPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        validarRangoFechas(fechaInicio, fechaFin);

        byte[] pdf = reporteExportService.generarPdfMovimientos(
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(23, 59, 59)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-movimientos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/movimientos/export/excel")
    public ResponseEntity<byte[]> exportarMovimientosExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        validarRangoFechas(fechaInicio, fechaFin);

        byte[] excel = reporteExportService.generarExcelMovimientos(
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(23, 59, 59)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-movimientos.xlsx")
                .contentType(EXCEL_MEDIA_TYPE)
                .body(excel);
    }

    // ======================== FACTURAS ========================

    @GetMapping("/facturas/{idVenta}/export/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> exportarFacturaPdf(@PathVariable UUID idVenta) {
        try {
            byte[] pdf = facturaPdfService.generarPdfFactura(idVenta);
            String num = "FAC-" + idVenta.toString().replace("-", "").substring(24).toUpperCase();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=factura-" + num + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void validarRangoFechas(LocalDate inicio, LocalDate fin) {
        if (inicio.getYear() < 2020) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior al año 2020.");
        }
        if (fin.isBefore(inicio)) {
            throw new RuntimeException("La fecha final no puede ser menor a la fecha de inicio.");
        }
    }
}
