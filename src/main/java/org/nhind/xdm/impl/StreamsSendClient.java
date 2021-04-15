package org.nhind.xdm.impl;

import java.util.LinkedList;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.nhind.xdm.streams.SmtpGatewayMessageSource;
import org.nhindirect.common.mail.SMTPMailMessage;
import org.nhindirect.xd.common.DirectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(value="direct.xd.usestreams", havingValue = "true", matchIfMissing=false)
public class StreamsSendClient extends AbstractSendClient
{
	@Autowired
	protected SmtpGatewayMessageSource source;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StreamsSendClient.class);	
	
	public StreamsSendClient()
	{
		
	}

	@Override
	public void send(DirectMessage message, String messageId, String suffix) throws MessagingException 
	{
		LOGGER.info("Building mess");
		
		final MimeMessage msg = buildMimeMessage(message, messageId, suffix, null);
		
		final List<InternetAddress> recipients = new LinkedList<>();
		for (Address recip : msg.getAllRecipients())
		{
			recipients.add((InternetAddress)recip);
		}
		
		final SMTPMailMessage mailMessage = new SMTPMailMessage(msg, recipients, (InternetAddress)msg.getFrom()[0]);
		final String messageid = msg.getMessageID();
		source.forwardSMTPMessage(mailMessage);
		
		LOGGER.info("Successfully sent message with message id {} to STA via streams.", messageid);
	}
	
}
