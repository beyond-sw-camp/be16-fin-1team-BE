package com.Dolmeng_E.workspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(
		basePackages = {
				"com.Dolmeng_E.workspace",
				"com.example.modulecommon"
		},
		// s3 일단 차단
		excludeFilters = {
				@ComponentScan.Filter(
						type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
						classes = {
								com.example.modulecommon.config.AwsS3Config.class,
								com.example.modulecommon.service.S3Uploader.class
						}
				)
		}
)
public class WorkspaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkspaceApplication.class, args);
	}

}
