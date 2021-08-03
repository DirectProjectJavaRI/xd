package org.nhind.xdm.springconfig;

import org.nhind.config.rest.AddressService;
import org.nhind.config.rest.SettingService;
import org.nhind.config.rest.feign.AddressClient;

import org.nhind.config.rest.feign.SettingClient;
import org.nhind.config.rest.impl.DefaultAddressService;
import org.nhind.config.rest.impl.DefaultSettingService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"org.nhind.config.rest.feign"})
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
public class ConfigServiceClientConfig
{	
	
	@Bean
	@ConditionalOnMissingBean
	public SettingService settingService(SettingClient settingClient)
	{
		return new DefaultSettingService(settingClient);
	}	
	
	@Bean
	@ConditionalOnMissingBean
	public AddressService addressService(AddressClient addressClient)
	{
		return new DefaultAddressService(addressClient);
	}	
	
}
