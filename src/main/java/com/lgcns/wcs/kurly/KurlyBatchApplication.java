package com.lgcns.wcs.kurly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KurlyBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(KurlyBatchApplication.class, args);
	}

}
