package de.ines.repositories;

import de.ines.domain.Route;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "route", path = "routes")
public interface RouteRepository extends CrudRepository<Route, Long> {
    Route findByRealID(@Param("realID")int realID);
}

