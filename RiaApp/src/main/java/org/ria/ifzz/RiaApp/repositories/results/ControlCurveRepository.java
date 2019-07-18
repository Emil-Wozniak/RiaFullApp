package org.ria.ifzz.RiaApp.repositories.results;

import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ControlCurveRepository extends CrudRepository<ControlCurve, Long> {
}
