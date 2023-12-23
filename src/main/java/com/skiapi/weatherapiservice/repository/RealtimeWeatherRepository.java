package com.skiapi.weatherapiservice.repository;

import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {

    @Query("select r from RealtimeWeather r where r.locations.countryCode  = ?1 and" +
            " r.locations.cityName =?2")
    public RealtimeWeather findByCountryCodeAndCity(String countryCode, String city);

    @Query("select r from RealtimeWeather r where r.locationCode = ?1 and r.locations.trashed = false")
    public RealtimeWeather findByLocationCode(String code);

}
