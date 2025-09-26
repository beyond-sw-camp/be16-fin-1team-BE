package com.Dolmeng_E.workspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WorkspaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkspaceApplication.class, args);
	}

}
