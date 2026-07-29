package org.nhind.xdm.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import jakarta.mail.Session;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.nhind.xdm.impl.AbstractSendClient.MimeMessageResult;
import org.nhindirect.common.mail.MDNStandard;
import org.nhindirect.xd.common.DirectDocument2;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.common.DirectMessage;

/**
 * Verifies that {@link AbstractSendClient#buildMimeMessage} sets the Direct
 * "reliable and timely" delivery-notification headers correctly when requested.
 */
public class AbstractSendClientTest
{
    private static final String SENDER = "outgoing_from@ett-domain.example.org";
    private static final String RECIPIENT = "direct_to@sut-domain.example.org";

    private MimeMessageResult result;

    @AfterEach
    public void cleanup()
    {
        if (result != null && result.attachmentFile() != null)
            result.attachmentFile().delete();
    }

    @Test
    public void buildMimeMessage_whenFinalDestinationDeliveryRequested_setsDispositionNotificationHeadersToSender()
            throws Exception
    {
        final DirectMessage message = buildMessage(SENDER, RECIPIENT, true);

        result = new SmtpSendClient("smtp.example.org", "user", "pass")
                .buildMimeMessage(message, "test-message-id", "eml", Session.getInstance(new Properties()));

        assertEquals(SENDER,
                result.mimeMessage().getHeader(MDNStandard.Headers.DispositionNotificationTo, null),
                "Disposition-Notification-To must route back to the message's own sender, not its recipient(s)");
        assertEquals(MDNStandard.DispositionOption_TimelyAndReliable + "=optional,true",
                result.mimeMessage().getHeader(MDNStandard.Headers.DispositionNotificationOptions, null));
    }

    @Test
    public void buildMimeMessage_whenFinalDestinationDeliveryNotRequested_omitsDispositionNotificationHeaders()
            throws Exception
    {
        final DirectMessage message = buildMessage(SENDER, RECIPIENT, false);

        result = new SmtpSendClient("smtp.example.org", "user", "pass")
                .buildMimeMessage(message, "test-message-id", "eml", Session.getInstance(new Properties()));

        assertNull(result.mimeMessage().getHeader(MDNStandard.Headers.DispositionNotificationTo, null));
        assertNull(result.mimeMessage().getHeader(MDNStandard.Headers.DispositionNotificationOptions, null));
    }

    private static DirectMessage buildMessage(String sender, String recipient, boolean finalDestinationDeliveryRequested)
    {
        final DirectMessage message = new DirectMessage(sender, Arrays.asList(recipient));
        message.setSubject("XD* Originated Message");
        message.setBody("Please find the attached XDM file.");
        message.setFinalDestinationDeliveryRequested(finalDestinationDeliveryRequested);

        final DirectDocuments documents = new DirectDocuments();
        final DirectDocument2 doc = new DirectDocument2();
        doc.getMetadata().setMimeType("text/xml");
        doc.getMetadata().setUniqueId("2.25.111111111111");
        doc.setData("<ClinicalDocument>sample C-CDA content</ClinicalDocument>".getBytes(StandardCharsets.UTF_8));
        documents.getDocuments().add(doc);
        documents.getSubmissionSet().setSourceId(sender);
        documents.getSubmissionSet().setUniqueId("2.25.222222222222");
        message.setDirectDocuments(documents);

        return message;
    }
}
