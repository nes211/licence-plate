package pl.tdelektro.licenceplate;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@AllArgsConstructor
public class LicencePlateApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(LicencePlateApplication.class, args);


    }

}
