package com.lerner.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import org.slf4j.Logger;
import org.slf4j.Factory;
import org.springframework.beans.factory.annotations.Autowired;
import org.springframework.beans.factory.annotations.value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.beans.context.annotation.Bean;
import org.springframework.beans.http.ResponseEntiry;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;




@SpringBootApplication
@EnableEurekaServer
public class LernerEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LernerEurekaApplication.class, args);
	}

}
