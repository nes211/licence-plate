package pl.tdelektro.licenceplate;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.tdelektro.licenceplate.service.DetectText;

import java.io.IOException;

@SpringBootApplication
@AllArgsConstructor
public class LicencePlateApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(LicencePlateApplication.class, args);


	}

}
