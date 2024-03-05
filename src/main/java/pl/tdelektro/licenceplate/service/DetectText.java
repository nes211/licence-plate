package pl.tdelektro.licenceplate.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleAuthUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.google.cloud.vision.v1.Feature.Type;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DetectText {

    public static List<String> labelList = new ArrayList<>();
    public static List<AnnotateImageRequest> requests = new ArrayList<>();

    public static List<String> detectText(String filePath) throws IOException {

        List<AnnotateImageResponse> responses = makeRequest(Feature.Type.LABEL_DETECTION, filePath);
        labelList = requestFilter(responses);
        return labelList;
    }


    public static List<String> requestFilter( List<AnnotateImageResponse> responses){

        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.format("Error: %s%n", res.getError().getMessage());

            }

            // For full list of available annotations, see
            for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {

                if(annotation.getDescription().equals("Vehicle registration plate")){
                    System.out.format("Text: %s%n", annotation.getDescription());
                    labelList.add(annotation.getDescription());

                }


            }
        }

       return labelList;
    }


    public static  List<AnnotateImageResponse> makeRequest(Type featureType, String filePath) throws IOException {

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(featureType).build();

        String credentialsPath = "src/main/resources/licenceplaterecognition-416208-88ac5d18bf9e.json";
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));

        ImageAnnotatorClient client = ImageAnnotatorClient.create(ImageAnnotatorSettings
                .newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build());

        //ImageContext context = ImageContext.newBuilder().build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        return responses;
    }

}
