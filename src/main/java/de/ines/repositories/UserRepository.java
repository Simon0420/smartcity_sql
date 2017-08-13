package de.ines.repositories;

import de.ines.domain.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "user", path = "users")
public interface UserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByName(@Param("name") String name);
}
