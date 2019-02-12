package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
    Result findByFileName(String fileName);

    List<Result> findByDataIdOrderByFileName(String dataId);
    List<Result> findByFileNameOrderByDataId(String dataId);

    Result getById(Long id);
}
