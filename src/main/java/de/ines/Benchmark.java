package de.ines;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Benchmark {

    public void storageBenchmark() throws Exception {
        Connection c = null;
        Statement exe_stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/postgis_ines_v3",
                        "postgres", "ines");
        System.out.println("Opened database successfully");
        exe_stmt = c.createStatement();

        Random random = new Random();
        double latitude = 0;
        double longitude = 0;
        long date = 0;
        double heading = 0;
        double speed = 0;
        double acceleration = 0;
        String name = "";
        String uniqueID="";
        int routeID = 0;

        int maxPointsProcessed = 1;
        long overallTime = 0;

        ArrayList<String> user_stmnts = new ArrayList<>();
        ArrayList<String> route_stmnts = new ArrayList<>();
        ArrayList<String> point_stmnts = new ArrayList<>();

        String userstmnt = "INSERT INTO app_user(name) values('cooluser')";
        user_stmnts.add(userstmnt);
        exe_stmt.executeUpdate(userstmnt);

        while (maxPointsProcessed >500) {
            int routeLength = random.nextInt(500);
            Date startTime = new Date();
            routeID++;
            String stmnt = "INSERT INTO route(user_id) values (1)";
            route_stmnts.add(stmnt);
            exe_stmt.executeUpdate(stmnt);
            for (int i = 0; i < routeLength; i++) {
                UUID.randomUUID().toString();
                latitude = random.nextDouble();
                longitude = random.nextDouble();
                date = random.nextLong();
                heading = random.nextDouble();
                speed = random.nextDouble();
                acceleration = random.nextDouble();
                String pt_stmnt = "INSERT INTO gps_point (location, latitude, longitude, heading, speed, acceleration, date, route_id)"
                        + "VALUES ("+
                        "ST_SetSRID(ST_MakePoint("+latitude+", "+longitude+"), 4326),"+
                        latitude+", "+
                        longitude+", "+
                        heading+", "+
                        speed+", "+
                        acceleration+", "+
                        date+", "+
                        routeID
                        +")";
                point_stmnts.add(pt_stmnt);
                exe_stmt.executeUpdate(pt_stmnt);
            }
            maxPointsProcessed-=routeLength;
            Date endTime = new Date();
            overallTime+= (endTime.getTime()-startTime.getTime());
            System.out.println("Number of GpsPoints Processed: "+routeLength+" Number of Left Points to be processed: "+maxPointsProcessed+ " Time consumed by Database: "+(double)(endTime.getTime()-startTime.getTime())/1000);
        }

        Date startTime = new Date();
        routeID++;
        String stmnt = "INSERT INTO route(user_id) values (1)";
        route_stmnts.add(stmnt);
        exe_stmt.executeUpdate(stmnt);
        for (int i = 0; i < maxPointsProcessed; i++) {
            UUID.randomUUID().toString();
            latitude = random.nextDouble();
            longitude = random.nextDouble();
            date = random.nextLong();
            heading = random.nextDouble();
            speed = random.nextDouble();
            acceleration = random.nextDouble();
            String pt_stmnt = "INSERT INTO gps_point (location, latitude, longitude, heading, speed, acceleration, date, route_id)"
                    + "VALUES ("+
                    "ST_SetSRID(ST_MakePoint("+latitude+", "+longitude+"), 4326),"+
                    latitude+", "+
                    longitude+", "+
                    heading+", "+
                    speed+", "+
                    acceleration+", "+
                    date+", "+
                    routeID
                    +")";
            point_stmnts.add(pt_stmnt);
            exe_stmt.executeUpdate(pt_stmnt);
        }
        Date endTime = new Date();
        overallTime+= (endTime.getTime()-startTime.getTime());
        System.out.println("Number of GpsPoints Processed: "+maxPointsProcessed+"Number of Left Points to be Processed: "+maxPointsProcessed+" Time consumed by Database: "+(double)(endTime.getTime()-startTime.getTime())/1000);
        System.out.println((double)overallTime/1000);

        c.setAutoCommit(false);
        exe_stmt.close();
        c.commit();
        c.close();
    }

    private void distanceBenchmark() throws Exception {
        Connection c = null;
        Statement exe_stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/postgis_ines_v3",
                        "postgres", "ines");
        System.out.println("Opened database successfully");
        exe_stmt = c.createStatement();

        Date startTime = new Date();

        double latitude =0;
        double longitude = 0;
        int distance = 0;
        long overallTime = 0;

        for(int i = 0; i < 1; i++) {
            Random random = new Random();
            latitude = random.nextDouble();
            longitude = random.nextDouble();
            distance = random.nextInt(400);

            String stmt = "SELECT\n" +
                    "  latitude, \n" +
                    "  longitude,\n" +
                    "  date,\n" +
                    "  route_id,\n" +
                    "  ST_DISTANCE(location, ST_SetSRID(ST_POINT(" + latitude + ", " + longitude + "), 4326), true) as dist \n" +
                    "FROM gps_point\n" +
                    "ORDER BY location <-> ST_SetSRID(ST_POINT(" + latitude + ", " + longitude + "), 4326)\n" +
                    "LIMIT 1;";
            ResultSet rs = exe_stmt.executeQuery(stmt);

            System.out.println("Search nearest point of latitude: " + latitude + " and longitude: " + longitude);
            while (rs.next()) {
                Double lat = rs.getDouble("latitude");
                Double lng = rs.getDouble("longitude");
                Long time = rs.getLong("date");
                System.out.println(lat + "\t" + lng + "\t" + time);
            }

            Date endTime = new Date();
            overallTime += (endTime.getTime() - startTime.getTime());
        }

        System.out.println((double)overallTime/1000);

        c.setAutoCommit(false);
        exe_stmt.close();
        c.commit();
        c.close();
    }

    public static void main(String[] args) {
        Benchmark bench = new Benchmark();
        try {
            //bench.storageBenchmark();
            bench.distanceBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //do sth.
    }



}