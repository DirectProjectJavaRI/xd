package org.nhind.xdr.test;

import org.nhind.xdm.boot.XDApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class, 
	    DataSourceTransactionManagerAutoConfiguration.class, 
	    HibernateJpaAutoConfiguration.class})
@EnableEurekaClient
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
}
