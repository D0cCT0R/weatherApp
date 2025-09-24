package com.example.weatherApp.service.core;


import com.example.weatherApp.dto.WeatherDto;
import com.example.weatherApp.dto.WeatherGeocodingDto;
import com.example.weatherApp.exceptions.WeatherApiException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class WeatherServiceIntegrationTest {
    private MockWebServer mockWebServer;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        weatherService = new WeatherService(restClient);
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-key");
        ReflectionTestUtils.setField(weatherService, "units", "metric");
    }
    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getWeatherByCityParseBasicWeatherResponse() throws WeatherApiException {
        String mockResponse = """
            {
                "name": "Moscow",
                "main": {
                    "temp": 15
                },
                "coord": {
                    "lat": 55.75,
                    "lon": 37.61
                }
            }
            """;
            mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));
        WeatherDto result = weatherService.getWeatherByCity(55.75, 37.61);
        assertNotNull(result);
        assertEquals("Moscow", result.name());
        assertEquals(15, result.main().temp());
        assertEquals(55.75, result.coord().lat());
        assertEquals(37.61, result.coord().lon());
    }
    @Test
    void getWeatherByCityIgnoreUnknownFields() throws WeatherApiException {
        String mockResponseWithExtraFields = """
            {
                "name": "London",
                "main": {
                    "temp": 12,
                    "pressure": 1015,
                    "humidity": 85,
                    "temp_min": 10,
                    "temp_max": 14
                },
                "coord": {
                    "lat": 51.51,
                    "lon": -0.13
                },
                "weather": [
                    {
                        "id": 300,
                        "main": "Drizzle",
                        "description": "light intensity drizzle"
                    }
                ],
                "base": "stations",
                "visibility": 10000,
                "wind": {
                    "speed": 4.1,
                    "deg": 80
                },
                "clouds": {
                    "all": 90
                },
                "dt": 1485789600,
                "sys": {
                    "type": 1,
                    "id": 5091,
                    "message": 0.0103,
                    "country": "GB",
                    "sunrise": 1485762037,
                    "sunset": 1485794875
                },
                "id": 2643743,
                "cod": 200
            }
            """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponseWithExtraFields)
                .addHeader("Content-Type", "application/json"));
        WeatherDto result = weatherService.getWeatherByCity(51.51, -0.13);
        assertNotNull(result);
        assertEquals("London", result.name());
        assertEquals(12, result.main().temp());
        assertEquals(51.51, result.coord().lat());
        assertEquals(-0.13, result.coord().lon());
    }
    @Test
    void getWeatherByCity_ShouldHandleEmptyOrNullValues() throws Exception {
        String mockResponse = """
            {
                "name": null,
                "main": {
                    "temp": 20
                },
                "coord": null
            }
            """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));
        WeatherDto result = weatherService.getWeatherByCity(0, 0);
        assertNotNull(result);
        assertNull(result.name());
        assertEquals(20, result.main().temp());
        assertNull(result.coord());
    }
    @Test
    void getWeatherByCity_ShouldThrowException_When400BadRequest() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("Bad Request"));
        WeatherApiException exception = assertThrows(WeatherApiException.class,
                () -> weatherService.getWeatherByCity(55.75, 37.61));

        assertEquals("Weather service error: invalid parameters", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }
    @Test
    void getWeatherByCity_ShouldThrowException_When500ServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));
        WeatherApiException exception = assertThrows(WeatherApiException.class,
                () -> weatherService.getWeatherByCity(55.75, 37.61));
        assertEquals("Weather service temporary unavailable", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
    }
    @Test
    void searchLocationsBasicWeatherResponse() throws WeatherApiException {
        String mockResponse = """
        [
            {
                "lat": "34.21",
                "lon": "12.12"
            },
            {
                "lat": "23.12",
                "lon": "12.13"
            }
        ]
        """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));
        List<WeatherGeocodingDto> result = weatherService.searchLocations("London");
        assertEquals(2, result.size());
        assertEquals(34.21, result.getFirst().lat());
        assertEquals(12.12, result.getFirst().lon());
        assertEquals(23.12, result.get(1).lat());
        assertEquals(12.13, result.get(1).lon());
    }
    @Test
    void searchLocations_ShouldThrowException_When400BadRequest() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("Bad Request"));
        WeatherApiException exception = assertThrows(WeatherApiException.class,
                () -> weatherService.searchLocations("London"));

        assertEquals("Weather service error: invalid parameters", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }
    @Test
    void searchLocations_ShouldThrowException_When500ServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));
        WeatherApiException exception = assertThrows(WeatherApiException.class,
                () -> weatherService.searchLocations("London"));
        assertEquals("Weather service temporary unavailable", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
    }
}
