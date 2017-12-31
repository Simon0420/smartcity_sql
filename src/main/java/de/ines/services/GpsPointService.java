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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import scala.Int;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GpsPointService {

    @Autowired
    GpsPointRepository gpsPointRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public AmqpTemplate template;

    @Autowired
    public PostDatabaseStreamService postDatabaseStreamService;

    public Configuration config;
    public SessionFactory sessionFactory;
    public Session session;



    public void saveGpsPoint(GpsPoint gpsPoint){
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
        //perform db operations
        Query q = session.createSQLQuery(querystring);
        q.executeUpdate();
    }

    public String saveRoute(String jsonRoute){
        ArrayList<Integer> ids = new ArrayList<>();
        if(this.session == null || !this.session.isOpen()) {
            if(session == null){
                Configuration config = new Configuration();

                config.configure();
                this.sessionFactory = config.buildSessionFactory();
                this.session = sessionFactory.openSession();
            }
            if(!session.isOpen()){
                this.session = sessionFactory.openSession();
            }
        }

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
        route = routeRepository.save(route);

        if(route.id == null){
            route.setId(1l);
        }
        for(int i = 0; i < route.getRoute().length; i++){
            GpsPoint p = route.getRoute()[i];
            p.setRoute(route);
            saveGpsPoint(p);
            ids.add(p.realID);
        }
        postDatabaseStreamService.postDatabaseStreamRoute(ids);

        Date endTime = new Date();
        //System.out.println((double)(endTime.getTime()-startTime.getTime())/1000);

        //close session
        session.close();

        return "succesfull";
    }

    public List<GpsPoint> withinDistanceCall(double latitude, double longitude){
        return gpsPointRepository.withinDistanceCall(latitude, longitude);
    }

    public String upstreamRoute(String jsonRoute){
        template.convertAndSend("SmartCity-Exchange","UpStreamQueue",jsonRoute);
        return "succesfull;";
    }



    public void upStreamMessage(String message){
        saveRoute(message);
    }


}
