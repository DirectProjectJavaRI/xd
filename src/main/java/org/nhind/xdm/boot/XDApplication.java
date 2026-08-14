package org.nhind.xdm.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.nhind.xdm.springconfig", "org.nhind.xdm.streams", "org.nhind.xdm.impl"})
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
