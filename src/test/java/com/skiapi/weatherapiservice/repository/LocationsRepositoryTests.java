package com.skiapi.weatherapiservice.repository;

import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@ComponentScan(basePackages = {"com.skiapi.weatherapicommon"})
public class LocationsRepositoryTests {

    @Autowired
    private LocationsRepository locationsRepository;

    //This method tests if we are able to add locations or not
    @Test
    public void testAddSuccess() {
        Locations locations = new Locations();
        locations.setCityName("New York City");
        locations.setCode("NYC_USA");
        locations.setRegionName("New York");
        locations.setCountryCode("US");
        locations.setCountryName("United States of America");
        locations.setEnabled(true);

        Locations savedLocations = locationsRepository.save(locations);
        assertThat(savedLocations).isNotNull();
        assertThat(savedLocations.getCode()).isEqualTo("NYC_USA");
    }

    @Test
    public void testGetListOfLocationsSuccess() {
        List<Locations> listOfLocations = locationsRepository.findUntrashed();
        assertThat(listOfLocations).isNotEmpty();
        listOfLocations.forEach(System.out::println);
    }

    @Test
    public void testGetLocationNotFound() {
        String code = "ABC";
        Locations locations = locationsRepository.locationFindByCode(code);
        assertThat(locations).isNull();
    }

    @Test
    public void testGetLocationFoundSuccess() {
        String code = "NYC_USA";
        Locations locations = locationsRepository.locationFindByCode(code);
        assertThat(locations).isNotNull();
        assertThat(locations.getCode()).isEqualTo(code);
    }

    @Test
    public void testTrashSuccess() {
        String code = "TEST";
        locationsRepository.trashByCode(code);
        Locations locations = locationsRepository.locationFindByCode(code);
        assertThat(locations).isNull();
    }

    @Test
    public void testAddRealtimeWeatherData() {
        String code = "NYC_USA";
        Locations locations = locationsRepository.locationFindByCode(code);
        RealtimeWeather realtimeWeather = locations.getRealtimeWeather();
        if(realtimeWeather == null){
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocations(locations);
        }
        realtimeWeather.setHumidity(10);
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        realtimeWeather.setPrecipitation(20);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setTemperature(-2);
        realtimeWeather.setWindSpeed(5);

        locations.setRealtimeWeather(realtimeWeather);
        Locations updatedLocations = locationsRepository.save(locations);
        assertThat(updatedLocations.getRealtimeWeather().getLocationCode())
                .isEqualTo(locations.getCode());
    }
}
