package org.nhind.xdm.springconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration
{
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web) -> web.ignoring().requestMatchers("/actuator/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        // No HTTP-layer authentication is configured for this app (no UserDetailsService/
        // AuthenticationProvider bean exists) -- trust for the XDR SOAP endpoint is established
        // at the message level (S/MIME), not via Spring Security. "/services/**" is where CXF
        // publishes the endpoint (see CXFWSServiceConfig); it must stay reachable unauthenticated.
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
