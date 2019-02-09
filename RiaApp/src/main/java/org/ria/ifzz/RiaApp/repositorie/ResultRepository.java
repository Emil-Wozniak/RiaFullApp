package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Result findByFileName(String fileName);

    Result findBySamples(String byFileName);

    Result getById(Long id);
}
