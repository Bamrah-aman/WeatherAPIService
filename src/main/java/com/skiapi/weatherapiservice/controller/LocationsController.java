package com.skiapi.weatherapiservice.controller;

import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.service.LocationsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1/locations")
@ComponentScan(basePackages = {"com.skiapi.weatherapicommon.Entity"})
public class LocationsController {


    private LocationsService locationsService;

    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @PostMapping
    public ResponseEntity<Locations> addLocations(@RequestBody @Valid Locations locations){
        Locations addedLocations = locationsService.add(locations);
        URI uri = URI.create("v1/locations/"+locations.getCode());
        log.info("Adding Done");
        return ResponseEntity.created(uri).body(addedLocations);
    }

    @GetMapping
    public ResponseEntity<List<Locations>> getListOfLocations() {
        List<Locations> listOfLocations = locationsService.getListOfLocations();
        if(listOfLocations.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(listOfLocations);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Locations> getLocationByCode(@PathVariable("code") String code) {
        Locations locations = locationsService.getLocationsByCode(code);

        if(locations ==  null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locations);
    }

    @PutMapping
    public ResponseEntity<?> updateLocations(@RequestBody @Valid Locations locations) {
        try{
            Locations updatedLocations =  locationsService.updateLocations(locations);
            log.info("Updated Location Successfully");
            return ResponseEntity.ok(updatedLocations);
        }catch (LocationNotFoundException locationNotFoundException){
            return new ResponseEntity<>("Location not found for given location code", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {
        try {
            locationsService.deleteLocation(code);
            return ResponseEntity.noContent().build();
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
