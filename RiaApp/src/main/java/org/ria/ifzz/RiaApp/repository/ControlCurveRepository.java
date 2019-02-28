package org.ria.ifzz.RiaApp.repository;

import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlCurveRepository extends CrudRepository<ControlCurve, Long> {

    ControlCurve findByFileName(String fileName);
    ControlCurve getById(Long id);

    Iterable<ControlCurve> findByDataIdOrderByFileName(String dataId);
}
