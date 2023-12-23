package com.skiapi.weatherapiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import com.skiapi.weatherapiservice.exception.GeoLocationException;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.service.GeoLocationService;
import com.skiapi.weatherapiservice.service.RealtimeWeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RealtimeController.class)
public class RealtimeWeatherControllerTests {

    private static final String END_POINT_PATH = "/v1/realtime";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RealtimeWeatherService realtimeWeatherService;

    @MockBean
    private GeoLocationService geoLocationService;

    @Test
    public void testGetShouldReturn400BadRequest() throws Exception {
        Mockito.when(geoLocationService.getLocations(Mockito.anyString()))
                .thenThrow(GeoLocationException.class);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        Locations locations = Locations.builder().build();
        Mockito.when(geoLocationService.getLocations(Mockito.anyString()))
                .thenReturn(locations);
        Mockito.when(realtimeWeatherService.getByLocation(locations))
                        .thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception{
        Locations location = Locations.builder().code("DEL_IN")
                .cityName("New Delhi").countryCode("IN")
                .regionName("New Delhi").countryName("India").build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder().humidity(12)
                .lastUpdated(LocalDateTime.now()).precipitation(14)
                .status("Sunny Day").temperature(35).windSpeed(23)
                .build();
        realtimeWeather.setLocations(location);

        Mockito.when(geoLocationService.getLocations(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(Mockito.any())).thenReturn(realtimeWeather);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void testGetByLocationCodeReturn404NotFound() throws Exception {
        String locatioCode = "xyz";
        Mockito.when(realtimeWeatherService.getByLocationCode(locatioCode)).
                thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH+"/"+locatioCode)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testGetByLocationCodeReturn200OK() throws Exception {
        String locatioCode = "ASR_IN";
        Mockito.when(realtimeWeatherService.getByLocationCode(locatioCode)).
                thenReturn(getRealtimeWeather());
        mockMvc.perform(get(END_POINT_PATH+"/"+locatioCode)).andExpect(status().isOk()).andDo(print());
    }

    private Locations getLocation() {
        Locations locations = Locations.builder()
                .code("DEL_IN").cityName("Delhi")
                .countryCode("IN").countryName("India")
                .regionName("Karol Bagh").build();
        return locations;
    }

    private RealtimeWeather getRealtimeWeather() {
        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .locationCode("DEL_IN").humidity(16)
                .lastUpdated(LocalDateTime.now()).precipitation(2)
                .status("Clear").temperature(30).windSpeed(23)
                .locations(getLocation()).build();
        return realtimeWeather;
    }

}
