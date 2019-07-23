package org.ria.ifzz.RiaApp.repositories.results;

import lombok.NonNull;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ExaminationPointRepository extends CrudRepository<ExaminationPoint, Long> {

    List<ExaminationPoint> findAllByIdentifier(@NonNull String identifier);

    List<ExaminationPoint> findByProbeNumberAndIdentifierOrderByProbeNumber(@NonNull Integer probeNumber, @NonNull String identifier);
}
