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
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class ReporteExportService {

    // ======================== COLORES CORPORATIVOS BIKESHOP ========================

    /** Azul principal del logo — #0057A8 */
    private static final Color BRAND_BLUE          = new Color(0, 87, 168);
    /** Azul oscuro para sección "Salidas" en movimientos */
    private static final Color BRAND_BLUE_DARK     = new Color(24, 95, 165);
    /** Azul claro para filas alternas y resúmenes */
    private static final Color BRAND_BLUE_LIGHT    = new Color(232, 240, 251);
    /** Texto oscuro principal */
    private static final Color BRAND_DARK          = new Color(33, 37, 41);
    /** Texto secundario / subtítulos */
    private static final Color BRAND_GREY          = new Color(108, 117, 125);
    /** Rojo para stock crítico únicamente */
    private static final Color BRAND_DANGER        = new Color(220, 53, 69);
    /** Rojo claro para filas de stock crítico */
    private static final Color BRAND_DANGER_LIGHT  = new Color(253, 232, 234);
    /** Borde de celdas */
    private static final Color CELL_BORDER         = new Color(222, 226, 230);

    private final VentaRepository      ventaRepository;
    private final BicicletaRepository  bicicletaRepository;
    private final EntradaRepository    entradaRepository;
    private final SalidaRepository     salidaRepository;

    private static final DateTimeFormatter DATE_FMT      = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FOOTER_FMT    = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ======================== PDF: VENTAS ========================

    public byte[] generarPdfVentas(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            agregarHeaderPDF(document,
                    "Reporte de Ventas",
                    "Período: " + inicio.format(DATE_ONLY_FMT) + " — " + fin.format(DATE_ONLY_FMT));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 2f, 3f, 1.5f, 2f});

            agregarCeldaEncabezado(table, "Fecha");
            agregarCeldaEncabezado(table, "Cliente");
            agregarCeldaEncabezado(table, "Bicicleta(s)");
            agregarCeldaEncabezado(table, "Cantidad");
            agregarCeldaEncabezado(table, "Total");

            java.math.BigDecimal granTotal = java.math.BigDecimal.ZERO;
            int rowNum = 0;

            for (Venta venta : ventas) {
                Color bg = (rowNum % 2 == 0) ? Color.WHITE : BRAND_BLUE_LIGHT;

                table.addCell(crearCelda(venta.getFecha().format(DATE_FMT), bg));
                table.addCell(crearCelda(venta.getUsuario().getUserName(), bg));

                StringBuilder bicicletas = new StringBuilder();
                int cantidadTotal = 0;
                for (DetalleVenta detalle : venta.getDetalles()) {
                    if (bicicletas.length() > 0) bicicletas.append(", ");
                    bicicletas.append(detalle.getBicicleta().getMarca())
                              .append(" ")
                              .append(detalle.getBicicleta().getModelo());
                    cantidadTotal += detalle.getCantidad();
                }

                table.addCell(crearCelda(bicicletas.toString(), bg));
                table.addCell(crearCeldaCentrada(String.valueOf(cantidadTotal), bg));
                table.addCell(crearCeldaNegrita("$" + venta.getTotalVenta().toPlainString(), bg));
                granTotal = granTotal.add(venta.getTotalVenta());
                rowNum++;
            }

            document.add(table);

            agregarResumenBox(document,
                    "Total de ventas: " + ventas.size() +
                    "          Ingresos totales: $" + granTotal.toPlainString());

            agregarFooter(document);
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
            XSSFCellStyle headerStyle = crearEstiloEncabezado(workbook);
            XSSFCellStyle evenStyle   = crearEstiloFilaPar(workbook);
            XSSFCellStyle moneyStyle  = crearEstiloMoneda(workbook, null);
            XSSFCellStyle moneyEven   = crearEstiloMoneda(workbook, evenStyle);

            String[] columnas = {"Fecha", "Cliente", "Bicicleta(s)", "Cantidad",
                                 "Precio Unitario", "Total Detalle", "Total Venta"};

            agregarTituloExcel(sheet, workbook,
                    "Reporte de Ventas — " + inicio.format(DATE_ONLY_FMT) + " al " + fin.format(DATE_ONLY_FMT),
                    columnas.length);

            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(22);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 2;
            for (Venta venta : ventas) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    boolean par = (fila % 2 == 0);
                    Row row = sheet.createRow(fila++);

                    setCellStr(row, 0, venta.getFecha().format(DATE_FMT),              par ? evenStyle : null);
                    setCellStr(row, 1, venta.getUsuario().getUserName(),                par ? evenStyle : null);
                    setCellStr(row, 2, detalle.getBicicleta().getMarca() + " " +
                                       detalle.getBicicleta().getModelo(),              par ? evenStyle : null);
                    setCellInt(row, 3, detalle.getCantidad(),                           par ? evenStyle : null);

                    Cell precioCell = row.createCell(4);
                    precioCell.setCellValue(detalle.getPrecioUnitario().doubleValue());
                    precioCell.setCellStyle(par ? moneyEven : moneyStyle);

                    Cell totalDetalleCell = row.createCell(5);
                    totalDetalleCell.setCellValue(detalle.getTotalDetalle().doubleValue());
                    totalDetalleCell.setCellStyle(par ? moneyEven : moneyStyle);

                    Cell totalVentaCell = row.createCell(6);
                    totalVentaCell.setCellValue(venta.getTotalVenta().doubleValue());
                    totalVentaCell.setCellStyle(par ? moneyEven : moneyStyle);
                }
            }

            sheet.createFreezePane(0, 2);
            for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

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

            agregarHeaderPDF(document, "Reporte de Inventario", "Estado actual del stock");

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2f, 2f, 1.5f, 1f, 1.2f, 1.8f});

            agregarCeldaEncabezado(table, "Código");
            agregarCeldaEncabezado(table, "Marca");
            agregarCeldaEncabezado(table, "Modelo");
            agregarCeldaEncabezado(table, "Tipo");
            agregarCeldaEncabezado(table, "Stock");
            agregarCeldaEncabezado(table, "Stock Mín.");
            agregarCeldaEncabezado(table, "Valor Unit.");

            int rowNum = 0;
            for (Bicicleta bici : bicicletas) {
                boolean stockCritico = bici.getStock() < bici.getStockMinimo();
                Color bg = stockCritico ? BRAND_DANGER_LIGHT
                         : ((rowNum % 2 == 0) ? Color.WHITE : BRAND_BLUE_LIGHT);

                table.addCell(crearCelda(bici.getCodigo(), bg));
                table.addCell(crearCelda(bici.getMarca(), bg));
                table.addCell(crearCelda(bici.getModelo(), bg));
                table.addCell(crearCelda(bici.getTipo() != null ? bici.getTipo().name() : "-", bg));

                // Stock con alerta visual si es crítico
                String stockText = String.valueOf(bici.getStock());
                Font stockFont;
                if (stockCritico) {
                    stockFont = new Font(Font.HELVETICA, 9, Font.BOLD, BRAND_DANGER);
                    stockText += " ⚠";
                } else {
                    stockFont = new Font(Font.HELVETICA, 9, Font.NORMAL, BRAND_DARK);
                }
                PdfPCell stockCell = new PdfPCell(new Phrase(stockText, stockFont));
                stockCell.setBackgroundColor(bg);
                stockCell.setPadding(7f);
                stockCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                stockCell.setBorderColor(CELL_BORDER);
                stockCell.setBorderWidth(0.5f);
                table.addCell(stockCell);

                table.addCell(crearCeldaCentrada(String.valueOf(bici.getStockMinimo()), bg));
                table.addCell(crearCeldaNegrita("$" + bici.getValorUnitario().toPlainString(), bg));
                rowNum++;
            }

            document.add(table);

            Font legendFont = new Font(Font.HELVETICA, 8, Font.ITALIC, BRAND_DANGER);
            Paragraph leyenda = new Paragraph("⚠  Fila rosada indica stock por debajo del mínimo.", legendFont);
            leyenda.setSpacingBefore(8f);
            document.add(leyenda);

            agregarFooter(document);
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
            XSSFCellStyle headerStyle  = crearEstiloEncabezado(workbook);
            XSSFCellStyle evenStyle    = crearEstiloFilaPar(workbook);
            XSSFCellStyle criticoStyle = crearEstiloStockCritico(workbook);
            XSSFCellStyle moneyStyle   = crearEstiloMoneda(workbook, null);
            XSSFCellStyle moneyEven    = crearEstiloMoneda(workbook, evenStyle);
            XSSFCellStyle moneyCritico = crearEstiloMoneda(workbook, criticoStyle);

            String[] columnas = {"Código", "Marca", "Modelo", "Tipo",
                                 "Stock", "Stock Mínimo", "Valor Unitario", "Estado"};

            agregarTituloExcel(sheet, workbook, "Inventario BikeShop", columnas.length);

            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(22);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 2;
            for (Bicicleta bici : bicicletas) {
                boolean critico = bici.getStock() < bici.getStockMinimo();
                boolean par     = (fila % 2 == 0);
                Row row = sheet.createRow(fila++);

                XSSFCellStyle base  = critico ? criticoStyle : (par ? evenStyle : null);
                XSSFCellStyle money = critico ? moneyCritico : (par ? moneyEven : moneyStyle);

                setCellStr(row, 0, bici.getCodigo(),                                           base);
                setCellStr(row, 1, bici.getMarca(),                                            base);
                setCellStr(row, 2, bici.getModelo(),                                           base);
                setCellStr(row, 3, bici.getTipo() != null ? bici.getTipo().name() : "-",       base);
                setCellInt(row, 4, bici.getStock(),                                            base);
                setCellInt(row, 5, bici.getStockMinimo(),                                      base);

                Cell valorCell = row.createCell(6);
                valorCell.setCellValue(bici.getValorUnitario().doubleValue());
                valorCell.setCellStyle(money);

                setCellStr(row, 7, critico ? "⚠ CRÍTICO" : "✓ OK",                           base);
            }

            sheet.createFreezePane(0, 2);
            for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel de inventario", e);
        }
    }

    // ======================== PDF: MOVIMIENTOS ========================

    public byte[] generarPdfMovimientos(LocalDateTime inicio, LocalDateTime fin) {
        List<Entrada> entradas = entradaRepository.findByFechaBetween(inicio, fin);
        List<Salida>  salidas  = salidaRepository.findByFechaBetween(inicio, fin);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            agregarHeaderPDF(document,
                    "Reporte de Movimientos",
                    "Período: " + inicio.format(DATE_ONLY_FMT) + " — " + fin.format(DATE_ONLY_FMT));

            // === SECCIÓN ENTRADAS ===
            agregarSeccionTitulo(document, "Entradas de Stock  (" + entradas.size() + ")", BRAND_BLUE);

            PdfPTable tablaEntradas = new PdfPTable(5);
            tablaEntradas.setWidthPercentage(100);
            tablaEntradas.setWidths(new float[]{2f, 2.5f, 2.5f, 1f, 2f});
            tablaEntradas.setSpacingAfter(20f);

            agregarCeldaEncabezado(tablaEntradas, "Fecha");
            agregarCeldaEncabezado(tablaEntradas, "Bicicleta");
            agregarCeldaEncabezado(tablaEntradas, "Proveedor");
            agregarCeldaEncabezado(tablaEntradas, "Cantidad");
            agregarCeldaEncabezado(tablaEntradas, "Usuario");

            int rowNum = 0;
            for (Entrada entrada : entradas) {
                Color bg = (rowNum % 2 == 0) ? Color.WHITE : BRAND_BLUE_LIGHT;
                tablaEntradas.addCell(crearCelda(entrada.getFecha().format(DATE_FMT), bg));
                tablaEntradas.addCell(crearCelda(
                        entrada.getBicicleta().getMarca() + " " + entrada.getBicicleta().getModelo(), bg));
                tablaEntradas.addCell(crearCelda(entrada.getProveedor().getNombre(), bg));
                tablaEntradas.addCell(crearCeldaCentrada(String.valueOf(entrada.getCantidad()), bg));
                tablaEntradas.addCell(crearCelda(entrada.getUsuario().getUserName(), bg));
                rowNum++;
            }
            document.add(tablaEntradas);

            // === SECCIÓN SALIDAS ===
            agregarSeccionTitulo(document, "Salidas de Stock  (" + salidas.size() + ")", BRAND_BLUE_DARK);

            PdfPTable tablaSalidas = new PdfPTable(6);
            tablaSalidas.setWidthPercentage(100);
            tablaSalidas.setWidths(new float[]{2f, 2.5f, 1.5f, 1f, 2.5f, 2f});

            agregarCeldaEncabezadoColor(tablaSalidas, "Fecha",       BRAND_BLUE_DARK);
            agregarCeldaEncabezadoColor(tablaSalidas, "Bicicleta",   BRAND_BLUE_DARK);
            agregarCeldaEncabezadoColor(tablaSalidas, "Tipo Salida", BRAND_BLUE_DARK);
            agregarCeldaEncabezadoColor(tablaSalidas, "Cantidad",    BRAND_BLUE_DARK);
            agregarCeldaEncabezadoColor(tablaSalidas, "Observación", BRAND_BLUE_DARK);
            agregarCeldaEncabezadoColor(tablaSalidas, "Usuario",     BRAND_BLUE_DARK);

            rowNum = 0;
            for (Salida salida : salidas) {
                Color bg = (rowNum % 2 == 0) ? Color.WHITE : BRAND_BLUE_LIGHT;
                tablaSalidas.addCell(crearCelda(salida.getFecha().format(DATE_FMT), bg));
                tablaSalidas.addCell(crearCelda(
                        salida.getBicicleta().getMarca() + " " + salida.getBicicleta().getModelo(), bg));
                tablaSalidas.addCell(crearCelda(salida.getTipoSalida().name(), bg));
                tablaSalidas.addCell(crearCeldaCentrada(String.valueOf(salida.getCantidad()), bg));
                tablaSalidas.addCell(crearCelda(
                        salida.getObservacion() != null ? salida.getObservacion() : "—", bg));
                tablaSalidas.addCell(crearCelda(salida.getUsuario().getUserName(), bg));
                rowNum++;
            }
            document.add(tablaSalidas);

            agregarFooter(document);
            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generando PDF de movimientos", e);
        }
    }

    // ======================== EXCEL: MOVIMIENTOS ========================

    public byte[] generarExcelMovimientos(LocalDateTime inicio, LocalDateTime fin) {
        List<Entrada> entradas = entradaRepository.findByFechaBetween(inicio, fin);
        List<Salida>  salidas  = salidaRepository.findByFechaBetween(inicio, fin);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFCellStyle headerStyle = crearEstiloEncabezado(workbook);
            XSSFCellStyle evenStyle   = crearEstiloFilaPar(workbook);

            // === HOJA ENTRADAS ===
            Sheet hojaEntradas = workbook.createSheet("Entradas");
            String[] colEntradas = {"Fecha", "Bicicleta", "Proveedor", "Cantidad", "Usuario"};
            agregarTituloExcel(hojaEntradas, workbook, "Entradas de Stock", colEntradas.length);

            Row headerEntradas = hojaEntradas.createRow(1);
            headerEntradas.setHeightInPoints(22);
            for (int i = 0; i < colEntradas.length; i++) {
                Cell cell = headerEntradas.createCell(i);
                cell.setCellValue(colEntradas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 2;
            for (Entrada entrada : entradas) {
                boolean par = (fila % 2 == 0);
                Row row = hojaEntradas.createRow(fila++);
                setCellStr(row, 0, entrada.getFecha().format(DATE_FMT),                                   par ? evenStyle : null);
                setCellStr(row, 1, entrada.getBicicleta().getMarca() + " " + entrada.getBicicleta().getModelo(), par ? evenStyle : null);
                setCellStr(row, 2, entrada.getProveedor().getNombre(),                                     par ? evenStyle : null);
                setCellInt(row, 3, entrada.getCantidad(),                                                  par ? evenStyle : null);
                setCellStr(row, 4, entrada.getUsuario().getUserName(),                                     par ? evenStyle : null);
            }
            hojaEntradas.createFreezePane(0, 2);
            for (int i = 0; i < colEntradas.length; i++) hojaEntradas.autoSizeColumn(i);

            // === HOJA SALIDAS ===
            Sheet hojaSalidas = workbook.createSheet("Salidas");
            String[] colSalidas = {"Fecha", "Bicicleta", "Tipo Salida", "Cantidad", "Observación", "Usuario"};
            agregarTituloExcel(hojaSalidas, workbook, "Salidas de Stock", colSalidas.length);

            Row headerSalidas = hojaSalidas.createRow(1);
            headerSalidas.setHeightInPoints(22);
            for (int i = 0; i < colSalidas.length; i++) {
                Cell cell = headerSalidas.createCell(i);
                cell.setCellValue(colSalidas[i]);
                cell.setCellStyle(headerStyle);
            }

            fila = 2;
            for (Salida salida : salidas) {
                boolean par = (fila % 2 == 0);
                Row row = hojaSalidas.createRow(fila++);
                setCellStr(row, 0, salida.getFecha().format(DATE_FMT),                                      par ? evenStyle : null);
                setCellStr(row, 1, salida.getBicicleta().getMarca() + " " + salida.getBicicleta().getModelo(), par ? evenStyle : null);
                setCellStr(row, 2, salida.getTipoSalida().name(),                                            par ? evenStyle : null);
                setCellInt(row, 3, salida.getCantidad(),                                                     par ? evenStyle : null);
                setCellStr(row, 4, salida.getObservacion() != null ? salida.getObservacion() : "-",          par ? evenStyle : null);
                setCellStr(row, 5, salida.getUsuario().getUserName(),                                        par ? evenStyle : null);
            }
            hojaSalidas.createFreezePane(0, 2);
            for (int i = 0; i < colSalidas.length; i++) hojaSalidas.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel de movimientos", e);
        }
    }

    // ======================== PDF LAYOUT HELPERS ========================

    /**
     * Genera el logo BikeShop como PNG en memoria usando Java2D.
     * Reproduce el SVG del logo: icono de bicicleta + texto "BikeShop" en azul corporativo.
     */
    private byte[] buildLogoPng() throws IOException {
        int iconSize = 42;
        float scale = iconSize / 24f;
        int imgWidth = 190;
        int imgHeight = 52;
        int padTop = (imgHeight - iconSize) / 2;

        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        java.awt.Color blue = new java.awt.Color(0, 87, 168);
        g.setColor(blue);
        g.setStroke(new BasicStroke(1.8f * scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Escalar el viewBox SVG (0 0 24 24) al tamaño del icono
        g.translate(padTop, padTop);
        g.scale(scale, scale);

        // Ruedas
        g.draw(new Ellipse2D.Float(2.5f, 12.5f, 6f, 6f));   // cx=5.5 cy=15.5 r=3
        g.draw(new Ellipse2D.Float(15.5f, 12.5f, 6f, 6f));  // cx=18.5 cy=15.5 r=3

        // M5.5 15.5 L9 9 H14
        GeneralPath p1 = new GeneralPath();
        p1.moveTo(5.5f, 15.5f); p1.lineTo(9f, 9f); p1.lineTo(14f, 9f);
        g.draw(p1);

        // M14 9 L18.5 15.5
        GeneralPath p2 = new GeneralPath();
        p2.moveTo(14f, 9f); p2.lineTo(18.5f, 15.5f);
        g.draw(p2);

        // M9 15.5 L11.5 9
        GeneralPath p3 = new GeneralPath();
        p3.moveTo(9f, 15.5f); p3.lineTo(11.5f, 9f);
        g.draw(p3);

        // M5.5 15.5 H11.5
        GeneralPath p4 = new GeneralPath();
        p4.moveTo(5.5f, 15.5f); p4.lineTo(11.5f, 15.5f);
        g.draw(p4);

        // M13 6 H16.5 L14 9  (manubrio)
        GeneralPath p5 = new GeneralPath();
        p5.moveTo(13f, 6f); p5.lineTo(16.5f, 6f); p5.lineTo(14f, 9f);
        g.draw(p5);

        // cx=13 cy=5.5 r=0.5 fill (headset)
        g.setStroke(new BasicStroke(0));
        g.fill(new Ellipse2D.Float(12.5f, 5f, 1f, 1f));

        // Reset transform para el texto
        g.setTransform(new AffineTransform());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Texto "BikeShop"
        java.awt.Font font = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 26);
        g.setFont(font);
        g.setColor(blue);
        java.awt.FontMetrics fm = g.getFontMetrics();
        int textY = (imgHeight + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString("BikeShop", iconSize + 8, textY);

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", baos);
        return baos.toByteArray();
    }

    /**
     * Encabezado corporativo: logo a la izquierda, título + subtítulo a la derecha,
     * seguido de una barra azul separadora.
     */
    private void agregarHeaderPDF(Document document, String titulo, String subtitulo)
            throws DocumentException, IOException {

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1.8f, 4f});
        headerTable.setSpacingAfter(4f);

        // --- Celda logo ---
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        try {
            Image logo = Image.getInstance(buildLogoPng());
            logo.scaleToFit(150f, 55f);
            logoCell.addElement(logo);
        } catch (Exception ex) {
            Font fb = new Font(Font.HELVETICA, 16, Font.BOLD, BRAND_BLUE);
            logoCell.addElement(new Paragraph("BikeShop", fb));
        }
        headerTable.addCell(logoCell);

        // --- Celda título ---
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Font tFont = new Font(Font.HELVETICA, 20, Font.BOLD, BRAND_DARK);
        Paragraph pTitulo = new Paragraph(titulo, tFont);
        pTitulo.setAlignment(Element.ALIGN_RIGHT);
        titleCell.addElement(pTitulo);

        if (subtitulo != null && !subtitulo.isBlank()) {
            Font sFont = new Font(Font.HELVETICA, 10, Font.NORMAL, BRAND_GREY);
            Paragraph pSub = new Paragraph(subtitulo, sFont);
            pSub.setAlignment(Element.ALIGN_RIGHT);
            titleCell.addElement(pSub);
        }
        headerTable.addCell(titleCell);
        document.add(headerTable);

        // --- Barra azul separadora ---
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        lineTable.setSpacingAfter(18f);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBackgroundColor(BRAND_BLUE);
        lineCell.setFixedHeight(3.5f);
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineTable.addCell(lineCell);
        document.add(lineTable);
    }

    /**
     * Barra de sección coloreada (Entradas / Salidas en Movimientos).
     */
    private void agregarSeccionTitulo(Document document, String texto, Color color)
            throws DocumentException {
        Font font = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);

        PdfPTable bar = new PdfPTable(1);
        bar.setWidthPercentage(100);
        bar.setSpacingBefore(20f);
        bar.setSpacingAfter(8f);

        PdfPCell cell = new PdfPCell(new Phrase("   " + texto, font));
        cell.setBackgroundColor(color);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(7f);
        cell.setPaddingBottom(7f);
        bar.addCell(cell);
        document.add(bar);
    }

    /**
     * Caja de resumen con borde azul, alineada a la derecha.
     */
    private void agregarResumenBox(Document document, String texto)
            throws DocumentException {
        Font font = new Font(Font.HELVETICA, 11, Font.BOLD, BRAND_BLUE);

        PdfPTable box = new PdfPTable(1);
        box.setWidthPercentage(60);
        box.setHorizontalAlignment(Element.ALIGN_RIGHT);
        box.setSpacingBefore(18f);

        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(BRAND_BLUE_LIGHT);
        cell.setBorderColor(BRAND_BLUE);
        cell.setBorderWidth(1.5f);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.setPaddingLeft(14f);
        cell.setPaddingRight(14f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        box.addCell(cell);
        document.add(box);
    }

    /**
     * Pie de página con timestamp y nombre de empresa.
     */
    private void agregarFooter(Document document) throws DocumentException {
        Font font = new Font(Font.HELVETICA, 8, Font.ITALIC, BRAND_GREY);
        Paragraph footer = new Paragraph(
                "Documento generado el " + LocalDateTime.now().format(FOOTER_FMT) + "  ·  BikeShop",
                font);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(22f);
        document.add(footer);
    }

    // ======================== PDF CELL HELPERS ========================

    /** Encabezado de tabla en azul corporativo (por defecto). */
    private void agregarCeldaEncabezado(PdfPTable table, String texto) {
        agregarCeldaEncabezadoColor(table, texto, BRAND_BLUE);
    }

    /** Encabezado de tabla con color personalizado (variante para secciones de Movimientos). */
    private void agregarCeldaEncabezadoColor(PdfPTable table, String texto, Color bg) {
        Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(9f);
        cell.setPaddingBottom(9f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    /** Celda de datos con fondo y borde sutil. */
    private PdfPCell crearCelda(String texto, Color bg) {
        Font font = new Font(Font.HELVETICA, 9, Font.NORMAL, BRAND_DARK);
        PdfPCell cell = new PdfPCell(new Phrase(texto != null ? texto : "", font));
        cell.setPadding(7f);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(CELL_BORDER);
        cell.setBorderWidth(0.5f);
        return cell;
    }

    /** Celda centrada horizontalmente. */
    private PdfPCell crearCeldaCentrada(String texto, Color bg) {
        PdfPCell cell = crearCelda(texto, bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    /** Celda en negrita, alineada a la derecha (para valores monetarios). */
    private PdfPCell crearCeldaNegrita(String texto, Color bg) {
        Font font = new Font(Font.HELVETICA, 9, Font.BOLD, BRAND_DARK);
        PdfPCell cell = new PdfPCell(new Phrase(texto != null ? texto : "", font));
        cell.setPadding(7f);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(CELL_BORDER);
        cell.setBorderWidth(0.5f);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    // ======================== EXCEL HELPERS ========================

    /** Fila 0 del sheet: título fusionado con fondo azul corporativo. */
    private void agregarTituloExcel(Sheet sheet, XSSFWorkbook workbook,
                                    String titulo, int numColumnas) {
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(30);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(titulo);

        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(xssfColor(0, 87, 168));
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setColor(xssfColor(255, 255, 255));
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        titleStyle.setFont(font);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numColumnas - 1));
    }

    /** Estilo encabezado: fondo azul corporativo, texto blanco negrita. */
    private XSSFCellStyle crearEstiloEncabezado(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(xssfColor(0, 87, 168));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setColor(xssfColor(255, 255, 255));
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    /** Estilo filas pares: fondo azul muy claro. */
    private XSSFCellStyle crearEstiloFilaPar(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(xssfColor(232, 240, 251));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /** Estilo stock crítico: fondo rosado claro. */
    private XSSFCellStyle crearEstiloStockCritico(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(xssfColor(255, 235, 238));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Estilo moneda, opcionalmente heredando el fondo de otro estilo.
     * Si base != null, clona su fondo antes de aplicar el formato "$#,##0.00".
     */
    private XSSFCellStyle crearEstiloMoneda(XSSFWorkbook workbook, XSSFCellStyle base) {
        XSSFCellStyle style = workbook.createCellStyle();
        if (base != null) style.cloneStyleFrom(base);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        return style;
    }

    /** Construye un XSSFColor a partir de valores RGB. */
    private XSSFColor xssfColor(int r, int g, int b) {
        return new XSSFColor(new byte[]{(byte) r, (byte) g, (byte) b}, null);
    }

    // --- setCellValue overloads ---

    private void setCellStr(Row row, int col, String val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val != null ? val : "");
        if (style != null) cell.setCellStyle(style);
    }

    private void setCellInt(Row row, int col, int val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val);
        if (style != null) cell.setCellStyle(style);
    }
}
