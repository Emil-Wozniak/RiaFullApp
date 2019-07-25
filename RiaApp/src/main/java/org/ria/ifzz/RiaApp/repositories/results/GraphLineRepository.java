package org.ria.ifzz.RiaApp.repositories.results;

import lombok.NonNull;
import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GraphLineRepository extends CrudRepository<GraphLine, Long> {

    List<GraphLine> findAllByFilename(@NonNull String identifier);
}
