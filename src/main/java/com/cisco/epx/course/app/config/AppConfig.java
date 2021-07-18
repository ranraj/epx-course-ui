package com.ran.epx.course.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.PingHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	@Value("${epx.service.url}")
	private String serviceUrl;
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public PingHealthIndicator epxServicePing(RestTemplate restTemplate){
		return new PingHealthIndicator(){
			@Override
			protected void doHealthCheck(Health.Builder builder) throws Exception {
				ResponseEntity<Object> response =  restTemplate.getForEntity(serviceUrl + "/actuator/health",Object.class);
				if(response.getStatusCode()== HttpStatus.OK) {
					builder.up();
				} else {
					builder.down();
				}				
			}
		};
        
    }
		
}
