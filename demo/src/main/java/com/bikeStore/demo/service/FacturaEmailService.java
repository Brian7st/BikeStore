package com.bikeStore.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacturaEmailService {

    private final JavaMailSender    mailSender;
    private final FacturaPdfService facturaPdfService;

    @Value("${spring.mail.username}")
    private String mailFrom;

    /**
     * Genera el PDF de factura y lo envía como adjunto al correo indicado.
     * Se ejecuta en segundo plano (@Async) para no bloquear la respuesta al cliente.
     */
    @Async
    public void enviarFacturaPorCorreo(UUID idVenta, String emailCliente) {
        try {
            byte[] pdfBytes  = facturaPdfService.generarPdfFactura(idVenta);
            String numFac    = "FAC-" + idVenta.toString().replace("-", "").substring(24).toUpperCase();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(emailCliente);
            helper.setSubject("Gracias por tu compra — " + numFac);
            helper.setText(
                "Hola,\n\n" +
                "Gracias por tu compra en BikeShop. Adjunto encontrás tu factura.\n\n" +
                "Número de factura: " + numFac + "\n\n" +
                "Ante cualquier consulta no dudes en contactarnos.\n\n" +
                "Equipo BikeShop"
            );

            helper.addAttachment(
                numFac + ".pdf",
                new ByteArrayResource(pdfBytes),
                "application/pdf"
            );

            mailSender.send(message);
            log.info("Factura {} enviada correctamente a {}", numFac, emailCliente);

        } catch (Exception e) {
            // Error silencioso — la venta ya fue guardada, el correo es secundario
            log.error("No se pudo enviar la factura {} al correo {}: {}",
                    idVenta, emailCliente, e.getMessage());
        }
    }
}
