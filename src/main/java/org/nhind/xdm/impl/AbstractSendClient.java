package org.nhind.xdm.impl;

import java.io.IOException;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.nhind.xdm.MimeSendClient;
import org.nhindirect.xd.common.DirectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSendClient implements MimeSendClient
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSendClient.class);	
	
	public MimeMessage buildMimeMessage(DirectMessage message, String messageId, String suffix, Session session) throws MessagingException
	{
		LOGGER.info("Building Mime message for sender.");
		
        MimeMessage mmessage;
        Multipart mailBody;
        MimeBodyPart mainBody;
        MimeBodyPart mimeAttach;   
        
        InternetAddress addressFrom = new InternetAddress(message.getSender());

        InternetAddress[] addressTo = new InternetAddress[message.getReceivers().size()];
        int i = 0;
        for (String recipient : message.getReceivers())
        {
            addressTo[i++] = new InternetAddress(recipient);
        }

        // Build message object
        mmessage = new MimeMessage(session);
        mmessage.setFrom(addressFrom);
        mmessage.setRecipients(Message.RecipientType.TO, addressTo);
        mmessage.setSubject(message.getSubject());

        mailBody = new MimeMultipart();

        mainBody = new MimeBodyPart();
        mainBody.setDataHandler(new DataHandler(message.getBody(), "text/plain"));
        mailBody.addBodyPart(mainBody);

        try
        {
            mimeAttach = new MimeBodyPart();
            mimeAttach.attachFile(message.getDirectDocuments().toXdmPackage(messageId).toFile());
        }
        catch (IOException e)
        {
            throw new MessagingException("Unable to create/attach xdm zip file", e);
        }
        
        mailBody.addBodyPart(mimeAttach);

        mmessage.setContent(mailBody);
        
        return mmessage;
	}
}
