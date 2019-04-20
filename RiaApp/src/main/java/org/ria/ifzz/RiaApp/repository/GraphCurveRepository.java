package org.ria.ifzz.RiaApp.repository;

import org.ria.ifzz.RiaApp.domain.GraphCurve;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphCurveRepository extends CrudRepository<GraphCurve, Long> {

    Iterable<GraphCurve> findByDataIdOrderByFileName(String dataId);

    GraphCurve getByDataIdOrderByFileName(String dataId);

    GraphCurve findByFileName(String fileName);
}
