package org.ria.ifzz.RiaApp.repositories.results;

import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ExaminationPointRepository extends CrudRepository< ExaminationPoint, Long> {
}
