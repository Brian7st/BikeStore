package com.bikeStore.demo.service;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.DetalleVenta;
import com.bikeStore.demo.Entity.Entrada;
import com.bikeStore.demo.Entity.Salida;
import com.bikeStore.demo.Entity.Venta;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.EntradaRepository;
import com.bikeStore.demo.repository.SalidaRepository;
import com.bikeStore.demo.repository.VentaRepository;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteExportService {

    private final VentaRepository ventaRepository;
    private final BicicletaRepository bicicletaRepository;
    private final EntradaRepository entradaRepository;
    private final SalidaRepository salidaRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ======================== PDF: VENTAS ========================

    public byte[] generarPdfVentas(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(33, 37, 41));
            Paragraph titulo = new Paragraph("Reporte de Ventas", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Font subtitleFont = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(108, 117, 125));
            Paragraph periodo = new Paragraph(
                    "Período: " + inicio.format(DATE_ONLY_FMT) + " - " + fin.format(DATE_ONLY_FMT),
                    subtitleFont
            );
            periodo.setAlignment(Element.ALIGN_CENTER);
            periodo.setSpacingAfter(20f);
            document.add(periodo);

            // Tabla
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 2f, 3f, 2f, 2f});

            agregarCeldaEncabezado(table, "Fecha");
            agregarCeldaEncabezado(table, "Cliente");
            agregarCeldaEncabezado(table, "Bicicleta(s)");
            agregarCeldaEncabezado(table, "Cantidad");
            agregarCeldaEncabezado(table, "Total");

            java.math.BigDecimal granTotal = java.math.BigDecimal.ZERO;

            for (Venta venta : ventas) {
                table.addCell(crearCelda(venta.getFecha().format(DATE_FMT)));
                table.addCell(crearCelda(venta.getUsuario().getUserName()));

                // Concatenar bicicletas del detalle
                StringBuilder bicicletas = new StringBuilder();
                int cantidadTotal = 0;
                for (DetalleVenta detalle : venta.getDetalles()) {
                    if (bicicletas.length() > 0) bicicletas.append(", ");
                    bicicletas.append(detalle.getBicicleta().getMarca())
                            .append(" ")
                            .append(detalle.getBicicleta().getModelo());
                    cantidadTotal += detalle.getCantidad();
                }
                table.addCell(crearCelda(bicicletas.toString()));
                table.addCell(crearCelda(String.valueOf(cantidadTotal)));
                table.addCell(crearCelda("$" + venta.getTotalVenta().toPlainString()));
                granTotal = granTotal.add(venta.getTotalVenta());
            }

            document.add(table);

            // Resumen
            Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Paragraph resumen = new Paragraph(
                    "\nTotal de ventas: " + ventas.size() + "  |  Ingresos totales: $" + granTotal.toPlainString(),
                    totalFont
            );
            resumen.setSpacingBefore(15f);
            document.add(resumen);

            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generando PDF de ventas", e);
        }
    }

    // ======================== EXCEL: VENTAS ========================

    public byte[] generarExcelVentas(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Ventas");

            // Estilo encabezado
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle moneyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            moneyStyle.setDataFormat(format.getFormat("$#,##0.00"));

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"Fecha", "Cliente", "Bicicleta(s)", "Cantidad", "Precio Unitario", "Total Detalle", "Total Venta"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int fila = 1;
            for (Venta venta : ventas) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    Row row = sheet.createRow(fila++);
                    row.createCell(0).setCellValue(venta.getFecha().format(DATE_FMT));
                    row.createCell(1).setCellValue(venta.getUsuario().getUserName());
                    row.createCell(2).setCellValue(
                            detalle.getBicicleta().getMarca() + " " + detalle.getBicicleta().getModelo()
                    );
                    row.createCell(3).setCellValue(detalle.getCantidad());

                    Cell precioCell = row.createCell(4);
                    precioCell.setCellValue(detalle.getPrecioUnitario().doubleValue());
                    precioCell.setCellStyle(moneyStyle);

                    Cell totalDetalleCell = row.createCell(5);
                    totalDetalleCell.setCellValue(detalle.getTotalDetalle().doubleValue());
                    totalDetalleCell.setCellStyle(moneyStyle);

                    Cell totalVentaCell = row.createCell(6);
                    totalVentaCell.setCellValue(venta.getTotalVenta().doubleValue());
                    totalVentaCell.setCellStyle(moneyStyle);
                }
            }

            // Auto-ajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel de ventas", e);
        }
    }

    // ======================== PDF: INVENTARIO ========================

    public byte[] generarPdfInventario() {
        List<Bicicleta> bicicletas = bicicletaRepository.findAll();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(33, 37, 41));
            Paragraph titulo = new Paragraph("Reporte de Inventario", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20f);
            document.add(titulo);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2f, 2f, 1.5f, 1f, 1f, 1.5f});

            agregarCeldaEncabezado(table, "Código");
            agregarCeldaEncabezado(table, "Marca");
            agregarCeldaEncabezado(table, "Modelo");
            agregarCeldaEncabezado(table, "Tipo");
            agregarCeldaEncabezado(table, "Stock");
            agregarCeldaEncabezado(table, "Stock Mín.");
            agregarCeldaEncabezado(table, "Valor Unit.");

            for (Bicicleta bici : bicicletas) {
                boolean stockCritico = bici.getStock() < bici.getStockMinimo();
                Color bgColor = stockCritico ? new Color(255, 235, 238) : Color.WHITE;

                agregarCeldaConFondo(table, bici.getCodigo(), bgColor);
                agregarCeldaConFondo(table, bici.getMarca(), bgColor);
                agregarCeldaConFondo(table, bici.getModelo(), bgColor);
                agregarCeldaConFondo(table, bici.getTipo() != null ? bici.getTipo().name() : "-", bgColor);

                // Stock con indicador visual
                String stockText = String.valueOf(bici.getStock());
                if (stockCritico) stockText += " ⚠ CRÍTICO";
                agregarCeldaConFondo(table, stockText, bgColor);

                agregarCeldaConFondo(table, String.valueOf(bici.getStockMinimo()), bgColor);
                agregarCeldaConFondo(table, "$" + bici.getValorUnitario().toPlainString(), bgColor);
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generando PDF de inventario", e);
        }
    }

    // ======================== EXCEL: INVENTARIO ========================

    public byte[] generarExcelInventario() {
        List<Bicicleta> bicicletas = bicicletaRepository.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Inventario");
            CellStyle headerStyle = crearEstiloEncabezado(workbook);

            // Estilo para stock crítico (fondo rojo claro)
            CellStyle criticoStyle = workbook.createCellStyle();
            criticoStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            criticoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle moneyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            moneyStyle.setDataFormat(format.getFormat("$#,##0.00"));

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"Código", "Marca", "Modelo", "Tipo", "Stock", "Stock Mínimo", "Valor Unitario", "Estado"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 1;
            for (Bicicleta bici : bicicletas) {
                Row row = sheet.createRow(fila++);
                boolean critico = bici.getStock() < bici.getStockMinimo();

                Cell c0 = row.createCell(0); c0.setCellValue(bici.getCodigo());
                Cell c1 = row.createCell(1); c1.setCellValue(bici.getMarca());
                Cell c2 = row.createCell(2); c2.setCellValue(bici.getModelo());
                Cell c3 = row.createCell(3); c3.setCellValue(bici.getTipo() != null ? bici.getTipo().name() : "-");
                Cell c4 = row.createCell(4); c4.setCellValue(bici.getStock());
                Cell c5 = row.createCell(5); c5.setCellValue(bici.getStockMinimo());

                Cell c6 = row.createCell(6);
                c6.setCellValue(bici.getValorUnitario().doubleValue());
                c6.setCellStyle(moneyStyle);

                Cell c7 = row.createCell(7);
                c7.setCellValue(critico ? "⚠ CRÍTICO" : "OK");

                if (critico) {
                    for (int i = 0; i <= 7; i++) {
                        row.getCell(i).setCellStyle(criticoStyle);
                    }
                    // Re-aplicar moneyStyle con fondo rojo para la celda de valor
                    CellStyle moneyCritico = workbook.createCellStyle();
                    moneyCritico.cloneStyleFrom(moneyStyle);
                    moneyCritico.setFillForegroundColor(IndexedColors.ROSE.getIndex());
                    moneyCritico.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    c6.setCellStyle(moneyCritico);
                }
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel de inventario", e);
        }
    }

    // ======================== PDF: MOVIMIENTOS ========================

    public byte[] generarPdfMovimientos(LocalDateTime inicio, LocalDateTime fin) {
        List<Entrada> entradas = entradaRepository.findByFechaBetween(inicio, fin);
        List<Salida> salidas = salidaRepository.findByFechaBetween(inicio, fin);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(33, 37, 41));
            Font sectionFont = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(25, 135, 84));
            Font subtitleFont = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(108, 117, 125));

            Paragraph titulo = new Paragraph("Reporte de Movimientos", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph periodo = new Paragraph(
                    "Período: " + inicio.format(DATE_ONLY_FMT) + " - " + fin.format(DATE_ONLY_FMT),
                    subtitleFont
            );
            periodo.setAlignment(Element.ALIGN_CENTER);
            periodo.setSpacingAfter(20f);
            document.add(periodo);

            // === SECCIÓN ENTRADAS ===
            Paragraph secEntradas = new Paragraph("Entradas de Stock (" + entradas.size() + ")", sectionFont);
            secEntradas.setSpacingBefore(10f);
            secEntradas.setSpacingAfter(10f);
            document.add(secEntradas);

            PdfPTable tablaEntradas = new PdfPTable(5);
            tablaEntradas.setWidthPercentage(100);
            tablaEntradas.setWidths(new float[]{2f, 2.5f, 2.5f, 1f, 2f});

            agregarCeldaEncabezado(tablaEntradas, "Fecha");
            agregarCeldaEncabezado(tablaEntradas, "Bicicleta");
            agregarCeldaEncabezado(tablaEntradas, "Proveedor");
            agregarCeldaEncabezado(tablaEntradas, "Cantidad");
            agregarCeldaEncabezado(tablaEntradas, "Usuario");

            for (Entrada entrada : entradas) {
                tablaEntradas.addCell(crearCelda(entrada.getFecha().format(DATE_FMT)));
                tablaEntradas.addCell(crearCelda(
                        entrada.getBicicleta().getMarca() + " " + entrada.getBicicleta().getModelo()
                ));
                tablaEntradas.addCell(crearCelda(entrada.getProveedor().getNombre()));
                tablaEntradas.addCell(crearCelda(String.valueOf(entrada.getCantidad())));
                tablaEntradas.addCell(crearCelda(entrada.getUsuario().getUserName()));
            }
            document.add(tablaEntradas);

            // === SECCIÓN SALIDAS ===
            Font sectionSalidaFont = new Font(Font.HELVETICA, 13, Font.BOLD, new Color(220, 53, 69));
            Paragraph secSalidas = new Paragraph("Salidas de Stock (" + salidas.size() + ")", sectionSalidaFont);
            secSalidas.setSpacingBefore(25f);
            secSalidas.setSpacingAfter(10f);
            document.add(secSalidas);

            PdfPTable tablaSalidas = new PdfPTable(6);
            tablaSalidas.setWidthPercentage(100);
            tablaSalidas.setWidths(new float[]{2f, 2.5f, 1.5f, 1f, 2.5f, 2f});

            agregarCeldaEncabezado(tablaSalidas, "Fecha");
            agregarCeldaEncabezado(tablaSalidas, "Bicicleta");
            agregarCeldaEncabezado(tablaSalidas, "Tipo Salida");
            agregarCeldaEncabezado(tablaSalidas, "Cantidad");
            agregarCeldaEncabezado(tablaSalidas, "Observación");
            agregarCeldaEncabezado(tablaSalidas, "Usuario");

            for (Salida salida : salidas) {
                tablaSalidas.addCell(crearCelda(salida.getFecha().format(DATE_FMT)));
                tablaSalidas.addCell(crearCelda(
                        salida.getBicicleta().getMarca() + " " + salida.getBicicleta().getModelo()
                ));
                tablaSalidas.addCell(crearCelda(salida.getTipoSalida().name()));
                tablaSalidas.addCell(crearCelda(String.valueOf(salida.getCantidad())));
                tablaSalidas.addCell(crearCelda(
                        salida.getObservacion() != null ? salida.getObservacion() : "-"
                ));
                tablaSalidas.addCell(crearCelda(salida.getUsuario().getUserName()));
            }
            document.add(tablaSalidas);

            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generando PDF de movimientos", e);
        }
    }

    // ======================== EXCEL: MOVIMIENTOS ========================

    public byte[] generarExcelMovimientos(LocalDateTime inicio, LocalDateTime fin) {
        List<Entrada> entradas = entradaRepository.findByFechaBetween(inicio, fin);
        List<Salida> salidas = salidaRepository.findByFechaBetween(inicio, fin);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            CellStyle headerStyle = crearEstiloEncabezado(workbook);

            // === HOJA ENTRADAS ===
            Sheet hojaEntradas = workbook.createSheet("Entradas");
            Row headerEntradas = hojaEntradas.createRow(0);
            String[] colEntradas = {"Fecha", "Bicicleta", "Proveedor", "Cantidad", "Usuario"};
            for (int i = 0; i < colEntradas.length; i++) {
                Cell cell = headerEntradas.createCell(i);
                cell.setCellValue(colEntradas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 1;
            for (Entrada entrada : entradas) {
                Row row = hojaEntradas.createRow(fila++);
                row.createCell(0).setCellValue(entrada.getFecha().format(DATE_FMT));
                row.createCell(1).setCellValue(
                        entrada.getBicicleta().getMarca() + " " + entrada.getBicicleta().getModelo()
                );
                row.createCell(2).setCellValue(entrada.getProveedor().getNombre());
                row.createCell(3).setCellValue(entrada.getCantidad());
                row.createCell(4).setCellValue(entrada.getUsuario().getUserName());
            }
            for (int i = 0; i < colEntradas.length; i++) {
                hojaEntradas.autoSizeColumn(i);
            }

            // === HOJA SALIDAS ===
            Sheet hojaSalidas = workbook.createSheet("Salidas");
            Row headerSalidas = hojaSalidas.createRow(0);
            String[] colSalidas = {"Fecha", "Bicicleta", "Tipo Salida", "Cantidad", "Observación", "Usuario"};
            for (int i = 0; i < colSalidas.length; i++) {
                Cell cell = headerSalidas.createCell(i);
                cell.setCellValue(colSalidas[i]);
                cell.setCellStyle(headerStyle);
            }

            fila = 1;
            for (Salida salida : salidas) {
                Row row = hojaSalidas.createRow(fila++);
                row.createCell(0).setCellValue(salida.getFecha().format(DATE_FMT));
                row.createCell(1).setCellValue(
                        salida.getBicicleta().getMarca() + " " + salida.getBicicleta().getModelo()
                );
                row.createCell(2).setCellValue(salida.getTipoSalida().name());
                row.createCell(3).setCellValue(salida.getCantidad());
                row.createCell(4).setCellValue(
                        salida.getObservacion() != null ? salida.getObservacion() : "-"
                );
                row.createCell(5).setCellValue(salida.getUsuario().getUserName());
            }
            for (int i = 0; i < colSalidas.length; i++) {
                hojaSalidas.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel de movimientos", e);
        }
    }

    // ======================== HELPERS ========================

    private void agregarCeldaEncabezado(PdfPTable table, String texto) {
        Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(new Color(52, 58, 64)); // dark header
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8f);
        table.addCell(cell);
    }

    private PdfPCell crearCelda(String texto) {
        Font font = new Font(Font.HELVETICA, 9, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setPadding(6f);
        return cell;
    }

    private void agregarCeldaConFondo(PdfPTable table, String texto, Color bgColor) {
        Font font = new Font(Font.HELVETICA, 9, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(6f);
        table.addCell(cell);
    }

    private CellStyle crearEstiloEncabezado(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
}
