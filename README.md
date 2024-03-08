# Licence Plate Recognition Application Documentation

## 1. Introduction
<div class="wrap-text">
An application for license plate recognition utilizing Google Cloud Vision. The recognition process involves two stages. Initially, the captured image is processed to determine the presence of a license plate. If the probability of detecting a license plate exceeds 50%, the second stage of verification is initiated. Based on the obtained coordinates, the image is cropped. In the second stage, the cropped image is sent for text recognition from the license plate. The result is presented as JSON in the REST application endpoint.
<img alt="Licence plate recognition" src="https://github.com/nes211/licence-plate/blob/8a44db7ca07753ade3daedd16c0da5c93fa7d23d/img/licence_plate_diagram.jpg">

</div>



## 2. Getting Started
### Installation

    Clone the project repository from Git.
    Import the project into your preferred Java IDE (designed in IntelliJ 2023.3).
    Ensure you have the Java version 17 installed.

### Configuration
<div class="wrap-text">
    Place your Google Cloud API key file into the resources folder.
    <br> Modify API key JSON file name in application.properties for access your service.


<img alt="Application API Key path" src="https://github.com/nes211/licence-plate/blob/85a864c9f2f27a677eb8a3a34aaf84c7ae79cbfb/img/application_config.jpg">
    
</div>

## 3. Features

<div class="wrap-text">
For a photo, such as one taken by a dashboard camera, it is possible to recognize a license plate. If there are multiple license plates present, the first one with the highest probability of being correctly recognized is selected. If there are no license plates detected in the image, an information message is displayed.
</div>

## 4. Technologies Used

    Spring Boot Framework: For developing RESTful APIs with basic authorization features.
    Hibernate: For object-relational mapping and data storage.
    Lombok: For reducing boilerplate code.
    Field Validation: For validating input data like VIN number or password.
    Email Service: For sending email notifications.
    JUnit: For unit testing.
    RestAssured: For integration testing.
    Postman: For API testing and data input/output.
    Git: For version control.

## 5. Application Structure

    src/main/java: Contains the application source code.
    src/test/java: Contains unit and integration tests.
    src/main/resources: Contains configuration files.

## 6. Usage
<div class="wrap-text">
    Running the Application:
        Start the application locally using your IDE or by running mvn spring-boot:run in the terminal.
</div>

<div class="wrap-text">
    Interacting with the API:
        Use Postman to send POST requests to the /user/add endpoint.
        Authenticate user using the Postman GET request to endpoint /user/email/{userEmail} with valid credentials.
        Authenticate admin using the Postman all CRUD requests to all endpoints.
        <br>Postman requests file included in repo: <img alt="Postman request file" src="[https://github.com/nes211/Workshop/blob/249e7df7c38367da0c3878e1564520ffb0b7f1ad/img/structore.jpg](https://github.com/nes211/Workshop/blob/1f99ddf38881f278ac7fa78365b7f3c3bdbdc730/Workshop.postman_collection.json)">
</div>

<br>
<img alt="Postman queries" src="https://github.com/nes211/Workshop/blob/5637826ae8f2af306e69ecc3dc24b48aae1c8290/img/postman.jpg">
<br>
<br>

## 7. Authentication and Authorization

    Users can register in using their email adress and password via the /user/add endpoint.
    Authentication successful after proper credentials included in all requests.

## 8. Database Schema

### Table Structure
<img alt="Shows table structure" src="https://github.com/nes211/Workshop/blob/249e7df7c38367da0c3878e1564520ffb0b7f1ad/img/structore.jpg">

### Users Table
<img alt="Shows user table" src="https://github.com/nes211/Workshop/blob/249e7df7c38367da0c3878e1564520ffb0b7f1ad/img/users.jpg">

### Tasks Table
<img alt="Shows tasks table" src="https://github.com/nes211/Workshop/blob/249e7df7c38367da0c3878e1564520ffb0b7f1ad/img/tasks.jpg">


### Cars Table 
<img alt="Showscars table" src="https://github.com/nes211/Workshop/blob/249e7df7c38367da0c3878e1564520ffb0b7f1ad/img/cars.jpg">

## 9. Testing

### Unit Tests (JUnit)

    Unit tests are located in the src/test/java directory.
    Run tests using your IDE or mvn test command.

### Integration Tests (RestAssured)

    Integration tests are located in the src/test/java directory.
    Run tests using your IDE or mvn verify command.

## 10. Deployment

    Deploy the application to your preferred environment (local, cloud, etc.).
    Ensure proper configuration for the deployment environment.

## 11. Some problems during project
<div class="wrap-text">
    Vin crc verification. There is no only one crc algorithm for Vin number. Most comapany has them own crc algorithm. The most popular is Luna10 
    (inculde weight multiply system and modulo divide by 11 method).
</div>

## 12. Troubleshooting

  ### 1. Application Fails to Start

Issue: The application fails to start, and an error message is displayed.

Solution:
<div class="wrap-text">
    Check if all required dependencies are installed and configured correctly.
    Verify that the database connection settings in application.properties are accurate.
    Review the application logs for detailed error messages to pinpoint the issue.
</div>
 
 ### 2. Unable to Authenticate

Issue: Users are unable to authenticate and receive errors when attempting to log in.

Solution:

    Ensure that the user is registered.
    Ensure that the provided credentials are correct.
    Check the user roles and permissions to ensure proper access control.

  ### 3. Email Notifications Not Sending

Issue: Email notifications are not being sent to users upon repair completion.

Solution:
<div class="wrap-text">
    Verify that the email service configuration in email.properties is correct, including the SMTP server settings.
    Check if the email address provided for notifications is valid and accessible.
    Review the application logs for any errors related to the email service.
</div>

### 4. Database Connection Issues

Issue: The application encounters errors related to database connectivity.

Solution:
<div class="wrap-text">
    Check if the database server is running and accessible. H2 server is running all the time with application.
    Verify that the database credentials and connection settings in application.properties are accurate.
    Test the connection manually using a database client or command-line tool.
</div>
    
## 13. Future Improvements
<div class="wrap-text">
    Roadmap for future development includes adding additional features such as user management page.
    Feature requests and suggestions are encouraged.
</div>

## 14. License

    This project is licensed under the MIT License.

## 15. Contact

    For support or inquiries, contact the project maintainers at nes211nes211nes@gmail.com.
    Project repository: https://github.com/nes211/Workshop
