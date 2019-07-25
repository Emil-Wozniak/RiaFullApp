package org.ria.ifzz.RiaApp.repositories.results;

import lombok.NonNull;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ControlCurveRepository extends CrudRepository<ControlCurve, Long> {

    ControlCurve findByProbeNumberOrderByIdentifier(@NonNull Integer probeNumber);
    List<ControlCurve> findAllByIdentifier(@NonNull String identifier);
}
