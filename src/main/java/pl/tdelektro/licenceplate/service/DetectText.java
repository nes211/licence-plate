package pl.tdelektro.licenceplate.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.google.type.DateTime;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class DetectText {


    static ImageCropper imageCropper;
    public static List<String> labelList = new ArrayList<>();
    public static List<String> labelToDisplay = new ArrayList<>();
    public static List<AnnotateImageRequest> requests = new ArrayList<>();


    public List<String> detectText(String filePath) throws IOException {

        //First request for retrieve localization of registration plate
        List<AnnotateImageResponse> responses = makeRequest(Type.OBJECT_LOCALIZATION, filePath, "");
        labelList = requestFilter(responses);

        //If registration plate is recognised make request with cropped area
        if (labelList.get(0) != "No licence plate recognised") {
            List<AnnotateImageResponse> responsesWithBindingCords = makeRequest(
                    Type.TEXT_DETECTION,
                    filePath,
                    labelList.get(0)
            );
            labelList = requestFilter(responsesWithBindingCords);
        }


        //Retrieve data from labelList for complete response
        if (labelList.size() > 1) {
            labelToDisplay.add(labelList.get(1));
            labelToDisplay.add(labelList.get(2).replace("\n", " "));
        }

        return labelList.size() == 1 ? labelList : labelToDisplay;
    }


    public static List<String> requestFilter(List<AnnotateImageResponse> responses) {

        //Error response from Vision API
        responses.get(0).getFullTextAnnotation();
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.format("Error: %s%n", res.getError().getMessage());
            }

            //Processing first request with object detection for retrieve localization of licence plate
            for (LocalizedObjectAnnotation annotation : res.getLocalizedObjectAnnotationsList()) {

                //Licence plate string detection
                if (annotation.getMid().equals("/m/01jfm_")) {

                    if (annotation.getScore() > 0.5F) {
                        System.out.format("Label: %s%n", annotation.getBoundingPoly());
                        if (labelList.isEmpty()) {
                            labelList.add(annotation.getBoundingPoly().getNormalizedVerticesList().toString());
                            labelList.add(annotation.getName());
                        }
                    } else {
                        return labelList = Arrays.asList("No licence plate recognised");
                    }
                }
            }

            if (labelList.isEmpty()) {
                return labelList = Arrays.asList("No licence plate recognised");
            }

            //Processing second request for retrieve text detection from cropped image
            for (EntityAnnotation textAnnotation : res.getTextAnnotationsList()) {
                if (textAnnotation.getDescription() != null) {
                    labelList.add(textAnnotation.getDescription());
                }
            }
        }

        return labelList;
    }



    public static List<AnnotateImageResponse> makeRequest(Type featureType, String filePath, String vertices) throws IOException {

        //Second request, only for cropped image
        //New image filePath redirect to new cropped file
        //processed by BindingPoly vertices
        if (featureType.equals(Type.TEXT_DETECTION)) {
            filePath = imageCropper.croppImage(filePath, vertices);
        }


        //Define request params
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();

        Feature feat = Feature.newBuilder().setType(featureType).build();

        String credentialsPath = "src/main/resources/licenceplaterecognition-416208-88ac5d18bf9e.json";
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));

        ImageContext context = ImageContext.newBuilder()
                .addLanguageHints("pl")
                .build();

        ImageAnnotatorClient client = ImageAnnotatorClient.create(ImageAnnotatorSettings
                .newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build());

        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(feat)
                        .setImage(img)
                        .setImageContext(context)
                        .build();
        requests.add(request);

        //Send and retrieve response
        BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        return responses;
    }

}
