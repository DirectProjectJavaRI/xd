package org.nhind.xdm.impl;

import java.io.File;
import java.io.IOException;

import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.nhind.xdm.MimeSendClient;
import org.nhindirect.xd.common.DirectMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSendClient implements MimeSendClient
{
	/**
	 * The built MimeMessage along with the on-disk XDM zip file backing its attachment.
	 * The file remains on disk (in the process's working directory) until the message
	 * has actually been sent/serialized; callers must clean it up via
	 * {@link AbstractSendClient#deleteAttachmentFile(File)} once that's done.
	 */
	public record MimeMessageResult(MimeMessage mimeMessage, File attachmentFile) {}

	public MimeMessageResult buildMimeMessage(DirectMessage message, String messageId, String suffix, Session session) throws MessagingException
	{
		log.info("Building Mime message for sender.");
		
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

        final File xdmFile;
        try
        {
            xdmFile = message.getDirectDocuments().toXdmPackage(messageId).toFile();
            mimeAttach = new MimeBodyPart();
            mimeAttach.attachFile(xdmFile);
        }
        catch (IOException e)
        {
            throw new MessagingException("Unable to create/attach xdm zip file", e);
        }

        mailBody.addBodyPart(mimeAttach);

        mmessage.setContent(mailBody);

        return new MimeMessageResult(mmessage, xdmFile);
	}

	/**
	 * Deletes the on-disk XDM zip file created for a message attachment. Must only be
	 * called once the message has been fully sent/serialized, since the attachment is
	 * streamed lazily from this file.
	 */
	protected void deleteAttachmentFile(File attachmentFile)
	{
		if (attachmentFile != null && attachmentFile.exists() && !attachmentFile.delete())
		{
			log.warn("Unable to delete XDM attachment file {}", attachmentFile.getAbsolutePath());
		}
	}
}
