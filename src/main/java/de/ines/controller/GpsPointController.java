package de.ines.controller;

import de.ines.services.GpsPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class GpsPointController {
    final GpsPointService gpsPointService;

    @Autowired
    public GpsPointController(GpsPointService gpsPointService) {
        this.gpsPointService = gpsPointService;
    }

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public int getGPS_PlusByRealID(@RequestParam("id") String id){
        return 10;
    }

    @RequestMapping(value = "/saveRoute", method = RequestMethod.POST)
    public String saveRoute(@RequestBody String jsonRoute){
        return gpsPointService.saveRoute(jsonRoute);
    }

}
