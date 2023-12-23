package com.skiapi.weatherapiservice.service;

import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.repository.LocationsRepository;
import com.skiapi.weatherapiservice.repository.RealtimeWeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RealtimeWeatherService {

    @Autowired
    private RealtimeWeatherRepository repository;

    @Autowired
    private LocationsRepository locationsRepository;

    public RealtimeWeather getByLocation(Locations location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, cityName);
        if(realtimeWeather == null){
            throw  new LocationNotFoundException("No location found for country-code and city-name");
        }
        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException{
        RealtimeWeather realtimeWeather = repository.findByLocationCode(locationCode);
        if(realtimeWeather == null){
            throw new LocationNotFoundException("Location not found for the code: "+locationCode);
        }else {
            return realtimeWeather;
        }
    }

    public RealtimeWeather update(String locatioCode, RealtimeWeather realtimeWeather) throws Exception{
        Locations locations = locationsRepository.locationFindByCode(locatioCode);
        if(locations == null){
            throw new LocationNotFoundException("Location not found for the given code: "+locatioCode);
        }
        realtimeWeather.setLocations(locations);
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        return repository.save(realtimeWeather);
    }
}
