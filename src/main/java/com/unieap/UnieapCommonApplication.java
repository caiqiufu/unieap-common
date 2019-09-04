package com.unieap;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.task.DynamicScheduleTaskSecond;
import com.unieap.base.task.TaskProcess;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableConfigurationProperties
@ComponentScan(basePackages = { "com" })
@EnableJpaRepositories("com")
public class UnieapCommonApplication {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		SpringApplication.run(UnieapCommonApplication.class, args);
		TaskProcess taskProcess = (TaskProcess) ApplicationContextProvider.getBean("taskFileProcess");
		DynamicScheduleTaskSecond dynamicScheduleTaskSecond = new DynamicScheduleTaskSecond(taskProcess);
		dynamicScheduleTaskSecond.setCron("0/5 * * * * ?");
	}
}
