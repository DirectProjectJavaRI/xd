package org.nhind.xdr;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import jakarta.mail.Session;

import java.io.InputStream;
import java.util.Collections;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nhind.mail.service.DocumentRepository;
import org.nhind.mail.service.XDDeliveryCallback;
import org.nhind.mail.service.XDDeliveryCore;
import org.nhind.xdr.test.SpringBaseTest;
import org.nhindirect.common.mail.SMTPMailMessage;
import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.nhindirect.gateway.smtp.NotificationProducer;
import org.nhindirect.xd.common.SyntheticMetadataDefaults;
import org.nhindirect.xd.transform.impl.DefaultMimeXdsTransformer;

public class XDRSoapTest extends SpringBaseTest
{
	@Mock
	protected XDDeliveryCallback xdDeliveryCallback;
	
	@Mock
	protected NotificationProducer notificationProducer;
	
	@Test
	public void testSoapTransaction() throws Exception
	{
		final InternetAddress sendAddr = new InternetAddress("test@test.com");
		final InternetAddress recipAddr = new InternetAddress("recip@test.com");
		
		when(resolver.hasXdEndpoints(any())).thenReturn(true);
		when(resolver.getXdEndpoints(any())).thenReturn(Collections.singletonList(recipAddr.toString()));
		
		final byte[] xmdZip;
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("xdm/xdm.zip"))
		{
			xmdZip = is.readAllBytes();
		}
	    
		final MimeMultipart multipart = new MimeMultipart();
		
		final MimeBodyPart messageBodyPart = new MimeBodyPart();
		
	    final DataSource source = new ByteArrayDataSource(xmdZip, "application/zip");
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName("xdm.zip");
	    multipart.addBodyPart(messageBodyPart);
		
	
		
		final MimeMessage msg = new MimeMessage((Session)null);
		msg.setFrom(sendAddr);
		msg.addRecipient(RecipientType.TO, recipAddr);
		msg.setContent(multipart);
		msg.saveChanges();
		
		final SMTPMailMessage mailMessage = new SMTPMailMessage(msg, Collections.singletonList(recipAddr), sendAddr);
		
		final XDDeliveryCore deliveryCore = new XDDeliveryCore(resolver, xdDeliveryCallback, new DefaultTxDetailParser(),
				new DefaultMimeXdsTransformer(new SyntheticMetadataDefaults()), new DocumentRepository(), notificationProducer,
				"http://localhost:8080/xd/services/DocumentRepository_Service", Collections.emptyList());
		
		deliveryCore.processAndDeliverXDMessage(mailMessage);
		
	}
}
