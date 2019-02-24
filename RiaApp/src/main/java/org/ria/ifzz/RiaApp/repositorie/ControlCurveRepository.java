package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.ria.ifzz.RiaApp.domain.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlCurveRepository extends CrudRepository<ControlCurve, Long> {
    ControlCurve findByFileName(String fileName);

    List<Result> findByDataIdOrderByFileName(String dataId);
    List<Result> findByFileNameOrderByDataId(String dataId);

    Result getById(Long id);
}
