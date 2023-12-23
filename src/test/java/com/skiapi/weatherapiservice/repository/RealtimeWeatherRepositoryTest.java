package com.skiapi.weatherapiservice.repository;

import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@ComponentScan(basePackages = {"com.skiapi.weatherapicommon"})
public class RealtimeWeatherRepositoryTest {

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

    @Test
    public void testUpdate() {
        String id = "NYC_USA";
        Optional<RealtimeWeather> repository = realtimeWeatherRepository.findById(id);
            RealtimeWeather realtimeWeather = repository.get();
            realtimeWeather.setHumidity(20);
            realtimeWeather.setLastUpdated(LocalDateTime.now());
            realtimeWeather.setPrecipitation(30);
            realtimeWeather.setStatus("Raining");
            realtimeWeather.setTemperature(-3);
            realtimeWeather.setWindSpeed(10);

            assertThat(realtimeWeather.getHumidity()).isEqualTo(20);
    }

    @Test
    public void testFindByCountryCodeAndCityNotFound() {
        String countryCode = "abc";
        String cityName = "abc";

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);
        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByCountryCodeAndCityFound() {
        String countryCode = "US";
        String cityName = "New York City";

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);
        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocations().getCityName()).isEqualTo(cityName);
    }

    @Test
    public void testFindByLocationNotFound() {
        String locationCode = "xyz";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByTrashedLocationNotFound() {
        String locationCode = "NYC_USA";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByTrashedLocationFound() {
        String locationCode = "ASR_IN";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocationCode()).isEqualTo(locationCode);
    }
}
