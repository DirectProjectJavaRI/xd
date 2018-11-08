package org.nhind.xdm.springconfig;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;

import org.apache.cxf.jaxws.EndpointImpl;
import org.nhind.xdr.XDR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class CXFWSServiceConfig
{
	@Autowired
	protected Bus bus;
	
	@Bean 
	@ConditionalOnMissingBean
	public XDR xdSvc()
	{
		return new XDR();
	}	
	
    @Bean
	@ConditionalOnMissingBean    
    public Endpoint xdrEndpointService() 
    {
        final EndpointImpl endpoint = new EndpointImpl(bus, xdSvc());
        endpoint.publish("/DocumentRepository_Service");
        return endpoint;
    }
}
