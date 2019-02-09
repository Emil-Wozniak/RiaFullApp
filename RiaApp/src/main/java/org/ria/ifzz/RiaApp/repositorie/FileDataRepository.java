package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends CrudRepository<FileData, Long> {

    @Override
    Iterable<FileData> findAll();

    FileData getById(Long id);

}
