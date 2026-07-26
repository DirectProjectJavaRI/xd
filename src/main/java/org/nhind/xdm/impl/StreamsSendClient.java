package org.nhind.xdm.impl;

import java.util.LinkedList;
import java.util.List;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.nhind.xdm.streams.SmtpGatewayMessageSource;
import org.nhindirect.common.mail.SMTPMailMessage;
import org.nhindirect.xd.common.DirectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class StreamsSendClient extends AbstractSendClient
{
	@Autowired
	protected SmtpGatewayMessageSource source;
	
	public StreamsSendClient()
	{
		
	}

	@Override
	public void send(DirectMessage message, String messageId, String suffix) throws MessagingException 
	{
		log.info("Building mess");

		final MimeMessageResult result = buildMimeMessage(message, messageId, suffix, null);

		try
		{
			final MimeMessage msg = result.mimeMessage();

			msg.saveChanges();
			final List<InternetAddress> recipients = new LinkedList<>();
			for (Address recip : msg.getAllRecipients())
			{
				recipients.add((InternetAddress)recip);
			}

			final SMTPMailMessage mailMessage = new SMTPMailMessage(msg, recipients, (InternetAddress)msg.getFrom()[0]);
			final String messageid = msg.getMessageID();
			source.forwardSMTPMessage(mailMessage);

			log.info("Successfully sent message with message id {} to STA via streams.", messageid);
		}
		finally
		{
			deleteAttachmentFile(result.attachmentFile());
		}
	}
	
}
