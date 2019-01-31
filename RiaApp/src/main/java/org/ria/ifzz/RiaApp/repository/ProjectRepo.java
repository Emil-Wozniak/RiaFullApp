package org.ria.ifzz.RiaApp.repository;

import org.ria.ifzz.RiaApp.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectIdentifier);

    @Override
    List<Project> findAll();

    Iterable<Project> findAllByProjectLeader(String username);

}
