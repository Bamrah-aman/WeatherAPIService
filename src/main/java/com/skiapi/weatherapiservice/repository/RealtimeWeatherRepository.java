package com.skiapi.weatherapiservice.repository;

import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {

}
