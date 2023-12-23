package com.skiapi.weatherapiservice.service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapiservice.exception.GeoLocationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class GeoLocationService {

    private String DBPathIP2Bin = "C:\\Learning\\WeatherAPIProject\\WeatherAPIService\\ip2locationdb\\IP2LOCATION-LITE-DB3.BIN";
    private IP2Location ip2Location = new IP2Location();

    public GeoLocationService() {
        try {
            ip2Location.Open(DBPathIP2Bin, true);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public Locations getLocations(String ipAddress) throws GeoLocationException {
        try {
            IPResult result = ip2Location.IPQuery(ipAddress);

            if (!result.getStatus().equals("OK")) {
                throw new GeoLocationException
                        ("Geolocation failed with the status: " + result.getStatus());
            }
            log.info("Inside geolocation class returning the location behalf of IP");
            return new Locations
                    (result.getCity(), result.getRegion(), result.getCountryLong(),
                            result.getCountryShort());
        } catch (IOException e) {
            throw new GeoLocationException("Error while quering IP address", e);
        }
    }
}
