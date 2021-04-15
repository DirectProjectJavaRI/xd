package org.nhind.xdm.streams;


import org.nhindirect.common.mail.SMTPMailMessage;
import org.nhindirect.common.mail.streams.SMTPMailMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

@ConditionalOnProperty(value="direct.xd.usestreams", havingValue = "true", matchIfMissing=false)
@EnableBinding(SmtpGatewayMessageOutput.class)
public class SmtpGatewayMessageSource
{	
	protected static SmtpGatewayMessageSource messageSourceInstance;
	
	@Autowired
	@Qualifier(SmtpGatewayMessageOutput.SMTP_GATEWAY_MESSAGE_OUTPUT)
	private MessageChannel smtpGatewayChannel;
	
	public SmtpGatewayMessageSource()
	{
		messageSourceInstance = this;
	}
	
	@Output(SmtpGatewayMessageOutput.SMTP_GATEWAY_MESSAGE_OUTPUT)
	public <T> void forwardSMTPMessage(SMTPMailMessage msg) 
	{
		this.smtpGatewayChannel.send(SMTPMailMessageConverter.toStreamMessage(msg));
	}

	public static SmtpGatewayMessageSource getMessageSourceInstance()
	{
		return messageSourceInstance;
	}
}
