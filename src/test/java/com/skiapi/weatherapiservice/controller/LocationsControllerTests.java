package com.skiapi.weatherapiservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.service.LocationsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LocationsController.class)
public class LocationsControllerTests {

    private static final String END_POINT_PATH = "/v1/locations";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private LocationsService locationsService;

    @Test
    public void testAddShouldReturn400BadGateway() throws Exception {
        final Locations locations = Locations.builder().build();
        when(locationsService.add(locations)).thenReturn(locations);
        String contentAsStringJson = mapper.writeValueAsString(locations);

        mockMvc.perform(post(END_POINT_PATH).content(contentAsStringJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        when(locationsService.add(locations())).thenReturn(locations());
        String contentAsStringJson = mapper.writeValueAsString(locations());

        MockHttpServletResponse response = mockMvc.perform(post(END_POINT_PATH)
                        .content(contentAsStringJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        mockMvc.perform(post(END_POINT_PATH).content(contentAsStringJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("NYC_USA")))
                .andExpect(header().string("Location", is("v1/locations/NYC_USA")))
                .andDo(print());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void testGetListOfLocations204NoContentFound() throws Exception {
        when(locationsService.getListOfLocations()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH)).
                andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetListOfLocations200OK() throws Exception {
        when(locationsService.getListOfLocations()).
                thenReturn(List.of(locations(), locations1()));

        mockMvc.perform(get(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code", is("NYC_USA")))
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn405MethodNotAllowed() throws Exception {
        String path = END_POINT_PATH + "/asd";
        mockMvc.perform(post(path)).andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        String code = END_POINT_PATH + "/abcd";
        mockMvc.perform(get(code)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200Ok() throws Exception {
        String code = END_POINT_PATH + "/NYC_USA";
        Locations locations = locations();
        when(locationsService.getLocationsByCode(locations.getCode()))
                .thenReturn(locations);
        mockMvc.perform(get(code)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Locations updateLocations = locations1();
        when(locationsService.updateLocations(updateLocations)).
                thenThrow(new LocationNotFoundException("oops cannot update Kindly enter the correct location code"));
        String objectToJsonString = mapper.writeValueAsString(updateLocations);
        mockMvc.perform(put(END_POINT_PATH).content(objectToJsonString)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        Locations updateLocations = locations2();
        when(locationsService.updateLocations(updateLocations)).thenReturn(updateLocations);
        String objectToJsonString = mapper.writeValueAsString(updateLocations);
        mockMvc.perform(put(END_POINT_PATH).content(objectToJsonString)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200Ok() throws Exception {
        Locations updateLocations = locations();
        when(locationsService.updateLocations(updateLocations)).thenReturn(updateLocations);
        String objectToJsonString = mapper.writeValueAsString(updateLocations);
        mockMvc.perform(put(END_POINT_PATH).content(objectToJsonString)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteLocation404NotFound() throws Exception{
        String requestURI = END_POINT_PATH+"/NYC_USAa";
        doThrow(LocationNotFoundException.class).when(locationsService).
                deleteLocation(Mockito.anyString());
        mockMvc.perform(delete(requestURI)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteLocation204NoContent() throws Exception{
        String requestURI = END_POINT_PATH+"/NYC_USA";
        doNothing().when(locationsService).deleteLocation("NYC_USA");
        mockMvc.perform(delete(requestURI)).andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testValidateRequestBodyLocaionCodeNotNull() throws Exception {
        Locations locations = Locations.builder().
                cityName("abc").regionName("abc").countryName("abc")
                .enabled(true).build();
        String objectToJsonString = mapper.writeValueAsString(locations);
        mockMvc.perform(post(END_POINT_PATH).content(objectToJsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception{
        Locations locations = Locations.builder().code("dfkomglfkmeojokrmdlsfmf").countryCode("sdfsf")
                .cityName("abc").regionName("abc").countryName("abc")
                .enabled(true).build();
        String objectToJsonString = mapper.writeValueAsString(locations);
        mockMvc.perform(post(END_POINT_PATH).content(objectToJsonString)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }


    private final Locations locations() {
        return Locations.builder().code("NYC_USA").cityName("New York City")
                .countryCode("US").regionName("New York").enabled(true)
                .countryName("United States of America").build();
    }

    private final Locations locations1() {
        return Locations.builder().code("ASR_IN").cityName("Amritsar")
                .countryCode("IN").regionName("Purani Chungi").enabled(true)
                .countryName("India").build();
    }

    private final Locations locations2() {
        return Locations.builder().cityName("Amritsar")
                .countryCode("IN").regionName("Purani Chungi").enabled(true)
                .countryName("India").build();
    }

}
