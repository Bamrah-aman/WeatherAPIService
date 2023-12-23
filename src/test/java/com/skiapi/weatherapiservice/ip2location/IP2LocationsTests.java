package com.skiapi.weatherapiservice.ip2location;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class IP2LocationsTests {
    private String DBpathBinFile = "C:\\Learning\\WeatherAPIProject\\WeatherAPIService\\ip2locationdb\\IP2LOCATION-LITE-DB3.BIN";
    @Test
    public void testInvalidIP() throws IOException {
        IP2Location ip2Location = new IP2Location();
        ip2Location.Open(DBpathBinFile, true);

        String IpAddress = "abc";
        IPResult ipResult = ip2Location.IPQuery(IpAddress);
        assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");

        System.out.println(ipResult);
    }

    @Test
    public void testValidIP() throws IOException{
        IP2Location ip2Location = new IP2Location();
        ip2Location.Open(DBpathBinFile, true);

        String IpAddress = "180.188.250.12";
        IPResult ipResult = ip2Location.IPQuery(IpAddress);
        assertThat(ipResult.getStatus()).isEqualTo("OK");

        System.out.println(ipResult);
    }
}
