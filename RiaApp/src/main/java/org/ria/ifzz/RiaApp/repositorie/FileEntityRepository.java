package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileEntityRepository extends CrudRepository<FileEntity, Long> {

    @Override
    Iterable<FileEntity> findAll();

    @Override
    Iterable<FileEntity> findAllById(Iterable<Long> longs);

    FileEntity getById(Long id);

    FileEntity getByDataId(String dataId);

    FileEntity findByDataId(String dataId);

    FileEntity findByFileName(String fileName);
}
