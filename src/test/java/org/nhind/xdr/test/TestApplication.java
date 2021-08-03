package org.nhind.xdr.test;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import javax.mail.internet.InternetAddress;

import org.nhindirect.xd.routing.RoutingResolver;
import org.nhind.xdm.boot.XDApplication;
import org.nhind.xdm.streams.SmtpGatewayMessageSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class, 
	    DataSourceTransactionManagerAutoConfiguration.class, 
	    HibernateJpaAutoConfiguration.class,
	    R2dbcAutoConfiguration.class})
@ComponentScan(basePackages = {"org.nhind.xdm.springconfig", "org.nhind.xdm.streams", "org.nhind.xdm.impl"})
public class TestApplication extends SpringBootServletInitializer 
{
    public static void main(String[] args) 
    {
        SpringApplication.run(TestApplication.class, args);
    }  
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	{
	    return application.sources(XDApplication.class);
	}
	
	@Bean 
	@Primary
	public RoutingResolver mockResolver() throws Exception
	{
		final RoutingResolver resolver = mock(RoutingResolver.class);
	
		final InternetAddress recipAddr = new InternetAddress("recip@test.com");
		
		when(resolver.hasSmtpEndpoints(any())).thenReturn(true);
		when(resolver.getXdEndpoints(any())).thenReturn(Collections.singletonList(recipAddr.toString()));
		
		return resolver;
	}
	
	@Bean 
	@Primary
	public SmtpGatewayMessageSource smtpGatewayMessageSource()
	{
		return mock(SmtpGatewayMessageSource.class);
	}
}
