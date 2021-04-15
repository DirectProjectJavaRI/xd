package org.nhind.xdr;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import javax.mail.Session;

import java.util.Collections;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.mockito.Mock;
import org.nhind.mail.service.DocumentRepository;
import org.nhind.mail.service.XDDeliveryCallback;
import org.nhind.mail.service.XDDeliveryCore;
import org.nhind.xdr.test.SpringBaseTest;
import org.nhindirect.common.mail.SMTPMailMessage;
import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.nhindirect.gateway.smtp.NotificationProducer;
import org.nhindirect.xd.routing.RoutingResolver;
import org.nhindirect.xd.transform.impl.DefaultMimeXdsTransformer;

public class XDRSoapTest extends SpringBaseTest
{
	@Mock
	protected RoutingResolver resolver;
	
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
	
		
		final MimeMessage msg = new MimeMessage((Session)null);
		msg.setFrom(sendAddr);
		msg.addRecipient(RecipientType.TO, recipAddr);
		msg.setText("Text");
		msg.saveChanges();
		
		final SMTPMailMessage mailMessage = new SMTPMailMessage(msg, Collections.singletonList(recipAddr), sendAddr);
		
		final XDDeliveryCore deliveryCore = new XDDeliveryCore(resolver, xdDeliveryCallback, new DefaultTxDetailParser(), 
				new DefaultMimeXdsTransformer(), new DocumentRepository(), notificationProducer, "http://localhost:8080/services/DocumentRepository_Service");
		
		deliveryCore.processAndDeliverXDMessage(mailMessage);
	}
}
