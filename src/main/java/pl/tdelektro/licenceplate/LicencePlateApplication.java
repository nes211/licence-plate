package pl.tdelektro.licenceplate;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class LicencePlateApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicencePlateApplication.class, args);


		File imageFile = new File("src/main/resources/test4.jpg");
		ITesseract instance = new Tesseract();
		instance.setLanguage("pol");
		instance.setDatapath("src/main/resources/");

		try {
			String result = instance.doOCR(imageFile);
			System.out.println(result);
		} catch (TesseractException ex) {
			System.out.println(ex.getMessage());
		}








	}

}
