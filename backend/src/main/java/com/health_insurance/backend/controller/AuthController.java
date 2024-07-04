// package com.health_insurance.backend.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.HttpClientErrorException;
// import org.springframework.web.client.RestTemplate;

// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;

// import java.util.Map;
// import java.util.Base64;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000") // Allow CORS for all origins
// public class AuthController {

//     @Value("${cognito.clientId}")
//     private String clientId;

//     @Value("${cognito.clientSecret}")
//     private String clientSecret;

//     @Value("${cognito.domain}")
//     private String domain;

//     @Value("${frontendUri}")
//     private String frontendUri;

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @PostMapping("/auth")
//     public ResponseEntity<String> getToken(@RequestBody Map<String, String> requestBody) {
//         RestTemplate restTemplate = new RestTemplate();

//         // Prepare Basic Authentication header
//         String headerString = createBasicAuthHeader(clientId, clientSecret);

//         // Prepare headers for the request
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBasicAuth(headerString);
//         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

//         // Extract code from request body
//         String code = requestBody.get("code");

//         // Prepare form data for token exchange
//         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//         formData.add("grant_type", "authorization_code");
//         formData.add("client_id", clientId);
//         formData.add("redirect_uri", frontendUri);
//         formData.add("code", code);

//         HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

//         try {
//             // Perform POST request using RestTemplate
//             ResponseEntity<Map> responseEntity = restTemplate.exchange(
//                     domain + "/oauth2/token",
//                     HttpMethod.POST,
//                     requestEntity,
//                     Map.class
//             );

//             // Retrieve the response body
//             Map<String, Object> responseBody = responseEntity.getBody();

//             // Check if response body is not null
//             if (responseBody != null) {

//                 Object accessTokenObj = responseBody.get("access_token");
//                 if (accessTokenObj != null) {
//                     String accessToken = accessTokenObj.toString();
//                     String jsonResponse = objectMapper.writeValueAsString(Map.of("access_token", accessToken));
//                     // Return only the access token as ResponseEntity
//                     return ResponseEntity.ok(jsonResponse);
//                 } else {
//                     // Handle case where access_token is missing
//                     return ResponseEntity.badRequest().body("Access token not found in response");
//                 }
//                 // return ResponseEntity.ok(responseBody);
//             } else {
//                 // Handle case where response body is null
//                 return ResponseEntity.badRequest().body("Error communicating with Cognito");
//             }
//         } catch (HttpClientErrorException ex) {
//             // Log or handle specific RestTemplate exceptions
//             ex.printStackTrace();
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getMessage());
//         } catch (Exception ex) {
//             // Log or handle generic exceptions
//             ex.printStackTrace();
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
//         }
//     }

//     private String createBasicAuthHeader(String clientId, String clientSecret) {
//         String auth = clientId + ":" + clientSecret;
//         byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
//         return new String(encodedAuth);
//     }
// }
