package pl.tdelektro.licenceplate.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tdelektro.licenceplate.service.DetectText;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    private DetectText detectText;

    @GetMapping("/detect-text")
    public List<String> detectText() {
        try {
            return detectText.detectText("src/main/resources/test6.jpg");
        } catch (IOException e) {
            e.printStackTrace();
            return Arrays.asList("Error occurred during text detection: " + e.getMessage());
        }
    }
}