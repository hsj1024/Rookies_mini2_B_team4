package com.instagram;

import com.instagram.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// java utils 내용 선언
@EnableConfigurationProperties(JwtConfig.class)
@ComponentScan(basePackages = {"com.instagram"})

public class InstgramApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstgramApplication.class, args);
	}

}
