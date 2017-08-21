package de.ines.controller;

import de.ines.services.GpsPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
public class GpsPointController {
    final GpsPointService gpsPointService;

    @Autowired
    public GpsPointController(GpsPointService gpsPointService) {
        this.gpsPointService = gpsPointService;
    }

    @RequestMapping(value = "/saveRoute", method = RequestMethod.POST)
    public String saveRoute(@RequestBody String jsonRoute){
        return gpsPointService.saveRoute(jsonRoute);
    }

    @RequestMapping(value="/withinDistanceCall", method = RequestMethod.GET)
    public Iterable<Map<String,Object>> withinDistanceCall(@RequestParam(value="latitude")double latitude, @RequestParam(value="longitude")double longitude, @RequestParam(value="distance")int distance){
        return gpsPointService.withinDistanceCall(latitude, longitude, distance);
    }

}
