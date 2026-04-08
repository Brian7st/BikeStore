package com.bikeStore.demo.service;

import com.bikeStore.demo.Entity.DetalleVenta;
import com.bikeStore.demo.Entity.Venta;
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
import java.util.UUID;
import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class FacturaPdfService {

    private static final Color BRAND_BLUE       = new Color(0, 87, 168);
    private static final Color BRAND_BLUE_LIGHT = new Color(232, 240, 251);
    private static final Color BRAND_DARK       = new Color(33, 37, 41);
    private static final Color BRAND_GREY       = new Color(108, 117, 125);
    private static final Color CELL_BORDER      = new Color(222, 226, 230);

    private static final DateTimeFormatter DATE_FMT   = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FOOTER_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final VentaRepository ventaRepository;

    // ======================== PÚBLICO ========================

    public byte[] generarPdfFactura(UUID idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + idVenta));

        String numFactura = "FAC-" + idVenta.toString().replace("-", "").substring(24).toUpperCase();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36f, 36f, 36f, 36f);
            PdfWriter.getInstance(document, out);
            document.open();

            String fecha = venta.getFecha().format(DATE_FMT);
            agregarHeader(document, "FACTURA", "N° " + numFactura + "   |   Fecha: " + fecha);

            // === BLOQUE ASESOR / CLIENTE ===
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(18f);

            Font labelFont = new Font(Font.HELVETICA, 8, Font.BOLD, BRAND_BLUE);
            Font dataFont  = new Font(Font.HELVETICA, 9, Font.NORMAL, BRAND_DARK);
            Font nameBold  = new Font(Font.HELVETICA, 10, Font.BOLD, BRAND_DARK);

            PdfPCell asesorCell = new PdfPCell();
            asesorCell.setBackgroundColor(BRAND_BLUE_LIGHT);
            asesorCell.setBorderColor(CELL_BORDER);
            asesorCell.setBorderWidth(0.5f);
            asesorCell.setPadding(10f);
            asesorCell.addElement(new Paragraph("ASESOR DE VENTA", labelFont));
            asesorCell.addElement(new Paragraph(venta.getUsuario().getUserName(), nameBold));
            asesorCell.addElement(new Paragraph("Documento: " + venta.getUsuario().getDocument(), dataFont));
            if (venta.getUsuario().getTelefono() != null && !venta.getUsuario().getTelefono().isBlank()) {
                asesorCell.addElement(new Paragraph("Teléfono: " + venta.getUsuario().getTelefono(), dataFont));
            }
            infoTable.addCell(asesorCell);

            PdfPCell clienteCell = new PdfPCell();
            clienteCell.setBackgroundColor(BRAND_BLUE_LIGHT);
            clienteCell.setBorderColor(CELL_BORDER);
            clienteCell.setBorderWidth(0.5f);
            clienteCell.setPadding(10f);
            clienteCell.addElement(new Paragraph("CLIENTE", labelFont));
            clienteCell.addElement(new Paragraph("Consumidor Final", nameBold));
            infoTable.addCell(clienteCell);

            document.add(infoTable);

            // === TABLA DE ÍTEMS ===
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1.2f, 3.5f, 1.5f, 0.8f, 1.5f, 1.5f});

            crearCeldaEncabezado(tabla, "Código");
            crearCeldaEncabezado(tabla, "Producto");
            crearCeldaEncabezado(tabla, "Tipo");
            crearCeldaEncabezado(tabla, "Cant.");
            crearCeldaEncabezado(tabla, "Precio Unit.");
            crearCeldaEncabezado(tabla, "Total");

            for (int i = 0; i < venta.getDetalles().size(); i++) {
                DetalleVenta d = venta.getDetalles().get(i);
                Color bg = (i % 2 == 0) ? Color.WHITE : BRAND_BLUE_LIGHT;
                tabla.addCell(crearCelda(d.getBicicleta().getCodigo(), bg));
                tabla.addCell(crearCelda(d.getBicicleta().getMarca() + " " + d.getBicicleta().getModelo(), bg));
                tabla.addCell(crearCeldaCentrada(d.getBicicleta().getTipo().name(), bg));
                tabla.addCell(crearCeldaCentrada(String.valueOf(d.getCantidad()), bg));
                tabla.addCell(crearCeldaNegrita(String.format("$%,.2f", d.getPrecioUnitario()), bg));
                tabla.addCell(crearCeldaNegrita(String.format("$%,.2f", d.getTotalDetalle()), bg));
            }
            document.add(tabla);

            agregarResumenBox(document, "TOTAL:   " + String.format("$%,.2f", venta.getTotalVenta()));
            agregarFooter(document);

            document.close();
            return out.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generando PDF de factura", e);
        }
    }

    // ======================== HELPERS DE LAYOUT PDF ========================

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

        g.translate(padTop, padTop);
        g.scale(scale, scale);

        g.draw(new Ellipse2D.Float(2.5f, 12.5f, 6f, 6f));
        g.draw(new Ellipse2D.Float(15.5f, 12.5f, 6f, 6f));

        GeneralPath p1 = new GeneralPath();
        p1.moveTo(5.5f, 15.5f); p1.lineTo(9f, 9f); p1.lineTo(14f, 9f);
        g.draw(p1);

        GeneralPath p2 = new GeneralPath();
        p2.moveTo(14f, 9f); p2.lineTo(18.5f, 15.5f);
        g.draw(p2);

        GeneralPath p3 = new GeneralPath();
        p3.moveTo(9f, 15.5f); p3.lineTo(11.5f, 9f);
        g.draw(p3);

        GeneralPath p4 = new GeneralPath();
        p4.moveTo(5.5f, 15.5f); p4.lineTo(11.5f, 15.5f);
        g.draw(p4);

        GeneralPath p5 = new GeneralPath();
        p5.moveTo(13f, 6f); p5.lineTo(16.5f, 6f); p5.lineTo(14f, 9f);
        g.draw(p5);

        g.setStroke(new BasicStroke(0));
        g.fill(new Ellipse2D.Float(12.5f, 5f, 1f, 1f));

        g.setTransform(new AffineTransform());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

    private void agregarHeader(Document document, String titulo, String subtitulo)
            throws DocumentException, IOException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1.8f, 4f});
        headerTable.setSpacingAfter(4f);

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

    private void agregarResumenBox(Document document, String texto) throws DocumentException {
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

    private void agregarFooter(Document document) throws DocumentException {
        Font font = new Font(Font.HELVETICA, 8, Font.ITALIC, BRAND_GREY);
        Paragraph footer = new Paragraph(
                "Documento generado el " + LocalDateTime.now().format(FOOTER_FMT) + "  ·  BikeShop",
                font);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(22f);
        document.add(footer);
    }

    // ======================== HELPERS DE CELDA ========================

    private void crearCeldaEncabezado(PdfPTable table, String texto) {
        Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(BRAND_BLUE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(9f);
        cell.setPaddingBottom(9f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private PdfPCell crearCelda(String texto, Color bg) {
        Font font = new Font(Font.HELVETICA, 9, Font.NORMAL, BRAND_DARK);
        PdfPCell cell = new PdfPCell(new Phrase(texto != null ? texto : "", font));
        cell.setPadding(7f);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(CELL_BORDER);
        cell.setBorderWidth(0.5f);
        return cell;
    }

    private PdfPCell crearCeldaCentrada(String texto, Color bg) {
        PdfPCell cell = crearCelda(texto, bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

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
}
