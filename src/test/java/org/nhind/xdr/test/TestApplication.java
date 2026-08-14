package org.nhind.xdr.test;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import jakarta.mail.internet.InternetAddress;

import org.nhindirect.gateway.autoconfig.AuditorAutoConfiguration;
import org.nhindirect.gateway.autoconfig.ConfigServiceClientAutoConfiguration;
import org.nhindirect.gateway.autoconfig.DNSResolverAutoConfiguration;
import org.nhindirect.gateway.autoconfig.DSNGeneratorAutoConfiguration;
import org.nhindirect.gateway.autoconfig.KeyStoreProtectionMgrAutoConfiguration;
import org.nhindirect.gateway.autoconfig.MessageMonitorServiceAutoConfiguration;
import org.nhindirect.gateway.autoconfig.RouteResolverAutoConfiguration;
import org.nhindirect.gateway.autoconfig.SMTPAgentAutoConfiguration;
import org.nhindirect.gateway.autoconfig.SMTPGatewayFactoryAutoConfiguration;
import org.nhindirect.gateway.autoconfig.StreamsAutoConfiguration;
import org.nhind.xd.autoconfig.XDMailAutoConfiguration;
import org.nhindirect.xd.routing.RoutingResolver;
import org.nhind.xdm.boot.XDApplication;
import org.nhind.xdm.streams.SmtpGatewayMessageSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcAutoConfiguration;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

// gateway and xdmail are only test-scope dependencies here (for their delivery-core/notification
// classes, which this test constructs directly); their auto-configurations assume a standalone
// SMTP gateway app and must not run. direct-common-audit (pulled in transitively via gateway)
// also brings spring-boot-starter-data-r2dbc onto the test classpath with no connection
// configured, so its autoconfiguration must be excluded as well.
@SpringBootApplication(exclude = {
		AuditorAutoConfiguration.class,
		ConfigServiceClientAutoConfiguration.class,
		DNSResolverAutoConfiguration.class,
		DSNGeneratorAutoConfiguration.class,
		KeyStoreProtectionMgrAutoConfiguration.class,
		MessageMonitorServiceAutoConfiguration.class,
		RouteResolverAutoConfiguration.class,
		SMTPAgentAutoConfiguration.class,
		SMTPGatewayFactoryAutoConfiguration.class,
		StreamsAutoConfiguration.class,
		XDMailAutoConfiguration.class,
		R2dbcAutoConfiguration.class,
		R2dbcTransactionManagerAutoConfiguration.class})
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
	public SmtpGatewayMessageSource mockSmtpGatewayMessageSource()
	{
		return mock(SmtpGatewayMessageSource.class);
	}
}
