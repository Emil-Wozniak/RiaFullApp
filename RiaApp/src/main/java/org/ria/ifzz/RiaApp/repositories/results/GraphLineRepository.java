package org.ria.ifzz.RiaApp.repositories.results;

import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GraphLineRepository extends CrudRepository<GraphLine, Long> {
}
