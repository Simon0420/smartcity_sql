package de.ines.controller;

import de.ines.domain.GpsPoint;
import de.ines.services.GpsPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
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
    public String withinDistanceCall(@RequestParam(value="latitude")double latitude, @RequestParam(value="longitude")double longitude){
        List<GpsPoint> list = gpsPointService.withinDistanceCall(latitude, longitude);
        for(int i = 0; i < list.size(); i++) {
            GpsPoint pt = list.get(i);
            System.out.print((i+1)+". closest"+"Point: "+pt.latitude+", "+pt.longitude);
        }
        return "successful";
    }

}
