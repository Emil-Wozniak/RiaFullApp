package org.ria.ifzz.RiaApp.repository;

import org.ria.ifzz.RiaApp.domain.GraphCurve;
import org.ria.ifzz.RiaApp.domain.GraphCurveLines;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GraphCurveLinesRepository extends CrudRepository<GraphCurveLines, Long> {
    Iterable<GraphCurveLines> findByDataIdOrderByFileName(String dataId);
    Iterable<GraphCurveLines> findByDataIdOrderByGraphCurve(GraphCurve graphCurve);
    GraphCurveLines findByFileName(String fileName);

    Optional<GraphCurveLines> findById(Long id);
}
