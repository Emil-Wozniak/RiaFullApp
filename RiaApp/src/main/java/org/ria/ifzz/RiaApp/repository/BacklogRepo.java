package org.ria.ifzz.RiaApp.repository;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepo extends CrudRepository<Backlog, Long> {

    Backlog findByProjectIdentifier(String projectIdentifier);
}
