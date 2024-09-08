package com.east2west.service;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;

import java.io.IOException;

@Service
public class HotelSearchService {

    @Value("${serpapi.api.key}")
    private String apiKey;

    private final OkHttpClient httpClient;

    public HotelSearchService() {
        this.httpClient = new OkHttpClient();
    }

    public JsonObject searchHotels(String query, String checkInDate, String checkOutDate, int adults, int children) throws IOException {
        String url = String.format(
                "https://serpapi.com/search?api_key=%s&engine=google_hotels&q=%s&hl=en&gl=us&check_in_date=%s&check_out_date=%s&currency=USD&adults=%d&children=%d",
                apiKey, query, checkInDate, checkOutDate, adults, children
        );

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return JsonParser.parseString(responseBody).getAsJsonObject();
        }
    }
}