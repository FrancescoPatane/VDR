package it.saydigital.vdr;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VdrApplication {

	public static void main(String[] args) {
		File tempFolder = new File("temp");
		if (!tempFolder.exists() || !tempFolder.isDirectory()) {
			tempFolder.mkdir();
		}
		SpringApplication.run(VdrApplication.class, args);
	}
}
