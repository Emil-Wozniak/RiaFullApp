package org.ria.ifzz.RiaApp.repositories.results;

import lombok.NonNull;
import org.ria.ifzz.RiaApp.models.graph.Graph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GraphRepository extends CrudRepository<Graph, Long> {

    List<Graph> findAllByIdentifier(@NonNull String identifier);
}
