package com.east2west.controllers;

import com.east2west.models.DTO.MultiCityRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class FlightSearchController {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper;

    public FlightSearchController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("${serpapi.api.key}")
    private String apiKey;

    @PostMapping("/api/flights/{departureId}/{arrivalId}/{outboundDate}/{returnDate}/{tripType}/{travelClass}/{adults}/{children}/{infants}")
    public ResponseEntity<JsonNode> searchFlights(
            @PathVariable String departureId,
            @PathVariable String arrivalId,
            @PathVariable String outboundDate,
            @PathVariable String returnDate,
            @PathVariable String tripType,
            @PathVariable String travelClass,
            @PathVariable int adults,
            @PathVariable int children,
            @PathVariable int infants,
            @RequestBody(required = false) MultiCityRequest multiCityRequest
    ) {
        if (!isValidTripType(tripType)) {
            return createErrorResponse("Invalid `tripType` value. Allowed values are `one_way`, `round_trip`, or `multi_stage`.");
        }

        if ("round_trip".equalsIgnoreCase(tripType) && (returnDate == null || returnDate.isEmpty())) {
            return createErrorResponse("`return_date` is required for round-trip searches.");
        }

        Map<String, String> parameters = null;

        if(Objects.equals(tripType, "one_way") || Objects.equals(tripType,"round_trip")){
            parameters = prepareRequestParameters(departureId, arrivalId, outboundDate, returnDate,
                    tripType, travelClass, adults, children, infants);
        }

        if(Objects.equals(tripType, "multi_stage")){
            parameters = prepareRequestParametersMulti(multiCityRequest, departureId, arrivalId, outboundDate, returnDate,
                    tripType, travelClass, adults, children, infants);
        }


        assert parameters != null;
        String url = buildUrl(parameters);

        try {
            JsonNode responseJson = performApiRequest(url);

            if (responseJson.has("error")) {
                String errorMessage = responseJson.get("error").asText();
                if (errorMessage.contains("type") && errorMessage.contains("Round trip")) {
                    return createErrorResponse("Unexpected error: The API is interpreting the request as a round trip. Please check the API documentation for the correct `type` parameter usage.");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
            }

            return ResponseEntity.ok(responseJson);
        } catch (IOException e) {
            return handleApiError(e);
        }
    }

    private boolean isValidTripType(String tripType) {
        return "one_way".equalsIgnoreCase(tripType) ||
                "round_trip".equalsIgnoreCase(tripType) ||
                "multi_stage".equalsIgnoreCase(tripType);
    }

    private Map<String, String> prepareRequestParameters(String departureId, String arrivalId, String outboundDate,
                                                         String returnDate, String tripType, String travelClass,
                                                         int adults, int children, int infants) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("api_key", apiKey);
        parameters.put("engine", "google_flights");
        parameters.put("hl", "en");
        parameters.put("gl", "us");
        parameters.put("departure_id", departureId);
        parameters.put("arrival_id", arrivalId);
        parameters.put("outbound_date", outboundDate);
        parameters.put("currency", "USD");
        parameters.put("travel_class", mapClassesTypeToApiFormat(travelClass));
        parameters.put("adults", String.valueOf(adults));
        parameters.put("children", String.valueOf(children));
        parameters.put("infants", String.valueOf(infants));

        parameters.put("type", mapTripTypeToApiFormat(tripType));

        if ("round_trip".equalsIgnoreCase(tripType) && returnDate != null) {
            parameters.put("return_date", returnDate);
        }

        return parameters;
    }

    private Map<String, String> prepareRequestParametersMulti(
            MultiCityRequest multiCityRequest,
            String departureId, String arrivalId, String outboundDate,
            String returnDate, String tripType, String travelClass,
            int adults, int children, int infants) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("api_key", apiKey);
        parameters.put("engine", "google_flights");
        parameters.put("hl", "en");
        parameters.put("gl", "us");
        parameters.put("currency", "USD");
        parameters.put("travel_class", mapClassesTypeToApiFormat(travelClass));
        parameters.put("adults", String.valueOf(adults));
        parameters.put("children", String.valueOf(children));
        parameters.put("infants", String.valueOf(infants));

        if ("multi_stage".equalsIgnoreCase(tripType)) {
            String multiCityJson = createMultiCityJson(multiCityRequest);
            parameters.put("multi_city_json", multiCityJson);
            parameters.put("type", "3");
        } else {
            parameters.put("departure_id", departureId);
            parameters.put("arrival_id", arrivalId);
            parameters.put("outbound_date", outboundDate);

            if ("round_trip".equalsIgnoreCase(tripType) && returnDate != null) {
                parameters.put("return_date", returnDate);
            }

            parameters.put("type", mapTripTypeToApiFormat(tripType));
        }

        return parameters;
    }

    private String createMultiCityJson(MultiCityRequest multiCityRequest) {
        // Chuyển đổi danh sách đoạn đường bay thành chuỗi JSON
        try {
            return objectMapper.writeValueAsString(multiCityRequest.getSegments());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]"; // Trả về chuỗi JSON rỗng trong trường hợp lỗi
        }
    }


    private String mapTripTypeToApiFormat(String tripType) {
        switch (tripType.toLowerCase()) {
            case "one_way":
                return "2";
            case "round_trip":
                return "1";
            case "multi_stage":
                return "3";
            default:
                return "2"; // Default to one-way if unknown
        }
    }

    private String mapClassesTypeToApiFormat(String tripClasses) {
        switch (tripClasses.toLowerCase()) {
            case "special-popular":
                return "2";
            case "business-class":
                return "3";
            case "first-class":
                return "4";
            default:
                return "1";
        }
    }

    private String buildUrl(Map<String, String> parameters) {
        StringJoiner urlParams = new StringJoiner("&");
        parameters.forEach((key, value) -> urlParams.add(key + "=" + value));
        return "https://serpapi.com/search?" + urlParams.toString();
    }

    private JsonNode performApiRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return objectMapper.readTree(response.body().string());
            }
            throw new IOException("Empty response body");
        }
    }

    private ResponseEntity<JsonNode> handleApiError(IOException e) {
        e.printStackTrace();
        return createErrorResponse("Failed to retrieve flight data: " + e.getMessage());
    }

    private ResponseEntity<JsonNode> createErrorResponse(String errorMessage) {
        ObjectNode errorNode = objectMapper.createObjectNode().put("error", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorNode);
    }
}