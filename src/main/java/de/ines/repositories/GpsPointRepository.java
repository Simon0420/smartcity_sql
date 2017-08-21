package de.ines.repositories;

import de.ines.domain.GpsPoint;
import de.ines.domain.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Map;

/**
 * Created by simon on 29.07.2017.
 */
public interface GpsPointRepository extends CrudRepository<GpsPoint, Long> {
    GpsPoint findByRealID(@Param("realID")int realID);

    @Query(value = "INSERT INTO gps_point (acceleration, latitude, longitude, heading, speed, date, location, route_id) VALUES (:acc, :lat, :lng, :head, :speed, :time, ST_SetSRID(ST_MakePoint(:lng,:lat),26913), :route_id);", nativeQuery = true)
    public void insertGPSPoint(@Param("acc") double acc, @Param("lat") double lat, @Param("lng") double lng,
                               @Param("head") double head, @Param("speed") double speed, @Param("time") long time,
                               @Param("route_id") long route_id);

    @Query(value = "SELECT" +
            "  latitude," +
            "  longitude," +
            "  date," +
            "  route_id," +
            "  ST_DISTANCE(location, ST_SetSRID(ST_POINT({latitude}, {longitude}), 4326), true) as dist " +
            "FROM gps_point " +
            "ORDER BY location <-> ST_SetSRID(ST_POINT({latitude}, {longitude}), 4326) " +
            "LIMIT 1;", nativeQuery = true)
    Iterable<Map<String,Object>> withinDistanceCall(@Param("latitude")double latitude, @Param("longitude")double longitude, @Param("distance")int distance);
}