package it.saydigital.vdr;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import it.saydigital.vdr.util.EnvHandler;

@SpringBootApplication
@EnableAsync
public class VdrApplication {

	public static void main(String[] args) {
//		System.out.println(EnvHandler.getProperty("app.password_expiration_days"));
		File tempFolder = new File("temp");
		if (!tempFolder.exists() || !tempFolder.isDirectory()) {
			tempFolder.mkdir();
		}
		SpringApplication.run(VdrApplication.class, args);
	}
}
