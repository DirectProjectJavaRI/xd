package org.nhind.xdm.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"org.nhind.xdm.springconfig"})
public class XDApplication extends SpringBootServletInitializer 
{
    public static void main(String[] args) 
    {
        SpringApplication.run(XDApplication.class, args);
    }  
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	{
	    return application.sources(XDApplication.class);
	}	    
}
