package com.skiapi.weatherapiservice.service;

import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.repository.LocationsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class LocationsService {

    @Autowired
    private LocationsRepository locationsRepository;

    public Locations add(Locations locations){
        log.info("Adding the location using service layer method");
        return locationsRepository.save(locations);
    }

    public List<Locations> getListOfLocations() {
        log.info("getting the list of locations using service layer method");
        return locationsRepository.findUntrashed();
    }

    public Locations getLocationsByCode(String code) {
        log.info("Getting Location via Code using the service layer");
        return locationsRepository.locationFindByCode(code);
    }


    public Locations updateLocations(Locations locationsCode) throws LocationNotFoundException {
        Locations locations = locationsRepository.locationFindByCode(locationsCode.getCode());
        if(locations == null){
            throw new LocationNotFoundException("Location not found for code: "
                    +locationsCode.getCode());
        }
        log.info("Successfully found the locations for code: "+locations.getCode());
        log.info("Now Updating the Locations using service layer method");
        locations = Locations.builder().code(locationsCode.getCode()).
                cityName(locationsCode.getCityName())
                .regionName(locationsCode.getRegionName())
                .countryName(locationsCode.getCountryName())
                .countryCode(locationsCode.getCountryCode())
                .enabled(locationsCode.isEnabled())
                .build();
        return locationsRepository.save(locations);
    }

    public void deleteLocation(String code) throws LocationNotFoundException{
        Locations locations = locationsRepository.locationFindByCode(code);
        if (locations == null){
            throw new LocationNotFoundException("Location does not exist for the code: "+code);
        }
        locationsRepository.trashByCode(code);
    }
}
