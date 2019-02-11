package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Result findByFileName(String fileName);

    List<Result> findByDataIdOrderByFileName(String dataId);

    Result findByDataId(String dataId);
}
