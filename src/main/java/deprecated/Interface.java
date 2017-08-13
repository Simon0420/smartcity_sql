package deprecated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Deprecated
public class Interface {

    static int routeId = 33;

    public static void main(String args[]) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgis_ines",
                            "postgres", "ines");

            System.out.println("Opened database successfully");



            stmt = c.createStatement();



            String sql = "INSERT INTO ROUTES (ROUTEID, LENGTH, DURATION) "
                    + "VALUES ("+ routeId +",2, 2);";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO LOCATIONS (POINT, LATITUDE, LONGITUDE, HEAD, TIME, RouteID) "
                    + "VALUES (ST_SetSRID(ST_MakePoint(2.5, 3.2), 26913), 2.5, 3.2, 1.7, '2016-06-22 19:10:25-07', "+ routeId +");";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO LOCATIONS (POINT, LATITUDE, LONGITUDE, HEAD, TIME, RouteID) "
                    + "VALUES (ST_SetSRID(ST_MakePoint(2.5, 3.2), 26913), 2.5, 3.2, 1.7, '2016-06-22 19:10:25-07', "+ routeId +");";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO LOCATIONS (POINT, LATITUDE, LONGITUDE, HEAD, TIME, RouteID) "
                    + "VALUES (ST_SetSRID(ST_MakePoint(2.5, 3.2), 26913), 2.5, 3.2, 1.7, '2016-06-22 19:10:25-07', "+ routeId +");";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO LOCATIONS (POINT, LATITUDE, LONGITUDE, HEAD, TIME, RouteID) "
                    + "VALUES (ST_SetSRID(ST_MakePoint(2.5, 3.2), 26913), 2.5, 3.2, 1.7, '2016-06-22 19:10:25-07', "+ routeId +");";
            stmt.executeUpdate(sql);

            c.setAutoCommit(false);
            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}
