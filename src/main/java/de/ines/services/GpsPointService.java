package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import de.ines.domain.AppUser;
import de.ines.domain.GpsPoint;
import de.ines.domain.Route;
import de.ines.repositories.GpsPointRepository;
import de.ines.repositories.RouteRepository;
import de.ines.repositories.UserRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Date;

@Service
public class GpsPointService {

    @Autowired
    GpsPointRepository gpsPointRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    UserRepository userRepository;

    public GpsPoint searchGpsPointByRealID(int id){
        return gpsPointRepository.findByRealID(id);
    }

    public void saveGpsPoint(GpsPoint gpsPoint){
        //gpsPointRepository.insertGPSPoint(gpsPoint.acceleration,gpsPoint.latitude, gpsPoint.longitude, gpsPoint.heading,gpsPoint.speed,gpsPoint.date,1);
        //gpsPointRepository.save(gpsPoint);
        /*gpsPointRepository.save(new GpsPoint(gpsPoint.getDate(),
                gpsPoint.getLatitude(),
                gpsPoint.getLongitude(),
                gpsPoint.getHeading(),
                gpsPoint.getSpeed(),
                gpsPoint.getAcceleration(),
                wktToGeometry("POINT("+gpsPoint.getLatitude()+" "+gpsPoint.getLongitude()+")"),
                gpsPoint.getRoute(),1));*/

        String querystring = "INSERT INTO gps_point (location, latitude, longitude, heading, speed, acceleration, date, route_id)"
                + "VALUES ("+
                "ST_SetSRID(ST_MakePoint("+gpsPoint.latitude+", "+gpsPoint.longitude+"), 4326),"+
                gpsPoint.latitude+", "+
                gpsPoint.longitude+", "+
                gpsPoint.heading+", "+
                gpsPoint.speed+", "+
                gpsPoint.acceleration+", "+
                gpsPoint.date+", "+
                gpsPoint.route.id
                +")";

        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory = config.buildSessionFactory();

        //open new session
        Session newSession = sessionFactory.openSession();
        //perform db operations

        Query q = newSession.createSQLQuery(querystring);
        q.executeUpdate();

        //close session
        newSession.close();
    }

    public Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        System.out.println("geo mapping -done");
        return geom;
    }

    public String saveRoute(String jsonRoute){
        Date startTime = new Date();

        AppUser user = userRepository.findByName("user");
        if(user == null){
            user = new AppUser("user");
        }

        ObjectMapper mapper = new ObjectMapper();
        Route route = null;
        try {
            route = mapper.readValue(jsonRoute, Route.class);
        } catch (Exception e) {
            CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            e.printStackTrace(w);
            w.close();
            System.out.println(cw.toString());
            return cw.toString();
        }
        route.setUser(user);
        userRepository.save(user);
        long id = 0;
        if(routeRepository.save(route).id != null){
            id = routeRepository.save(route).id;
        }else{
            id = 0;
        }
        for(int i = 0; i < route.getRoute().length; i++){
            GpsPoint p = route.getRoute()[i];
            p.setRoute(route);
            saveGpsPoint(p);
        }

        Date endTime = new Date();
        System.out.println((double)(endTime.getTime()-startTime.getTime())/1000);
        return "Succesfull";
    }


}
