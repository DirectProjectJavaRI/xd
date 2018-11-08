package org.nhind.xdm.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"org.nhind.xdm.springconfig"})
public class XDApplication
{
    public static void main(String[] args) 
    {
        SpringApplication.run(XDApplication.class, args);
    }  
}
