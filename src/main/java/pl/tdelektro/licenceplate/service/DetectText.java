package pl.tdelektro.licenceplate.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class DetectText {


    static ImageCropper imageCropper;

    public static List<String> labelList = new ArrayList<>();
    public static List<String> labelToDisplay = new ArrayList<>();
    public static List<AnnotateImageRequest> requests = new ArrayList<>();

    public List<String> detectText(String filePath) throws IOException {

        List<AnnotateImageResponse> responses = makeRequest(Type.OBJECT_LOCALIZATION, filePath, "");
        labelList = requestFilter(responses);

        List<AnnotateImageResponse> responsesWithBindingCords = makeRequest(
                Type.TEXT_DETECTION,
                filePath,
                labelList.get(2)
        );

        labelList = requestFilter(responsesWithBindingCords);

        if(!labelList.isEmpty()) {
            labelToDisplay.add(labelList.get(1));
            labelToDisplay.add(labelList.get(6).replace("\n", " "));
        }

        return labelList.isEmpty() ? labelList = Arrays.asList("No licence plate recognised") : labelToDisplay;
    }


    public static List<String> requestFilter(List<AnnotateImageResponse> responses) {

        responses.get(0).getFullTextAnnotation();
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.format("Error: %s%n", res.getError().getMessage());
            }
            // For full list of available annotations, see
            for (LocalizedObjectAnnotation annotation : res.getLocalizedObjectAnnotationsList()) {
                if (annotation.getMid().equals("/m/01jfm_")) {

                    if (annotation.getScore() > 0.8F) {
                        System.out.format("Label: %s%n", annotation.getBoundingPoly());
                        labelList.add(annotation.getMid());
                        labelList.add(annotation.getName());
                        labelList.add(annotation.getBoundingPoly().getNormalizedVerticesList().toString());
                    } else {
                        return labelList = Arrays.asList("No licence plate recognised");
                    }

                }
            }


            for (EntityAnnotation textAnnotation : res.getTextAnnotationsList()) {
                if (textAnnotation.getDescription() != null) {
                    System.out.format("Text: %s%n", textAnnotation.getDescription());
                    labelList.add(textAnnotation.getDescription());
                }
            }
        }
        return labelList;
    }


    public static List<AnnotateImageResponse> makeRequest(Type featureType, String filePath, String vertices) throws IOException {

        //For cropped image filePath dir to new cropped file by BingingPoly vertices
        if (featureType.equals(Type.TEXT_DETECTION) ) {
            filePath = imageCropper.croppImage(filePath, vertices);
        }

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

        BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        return responses;
    }

}
