package org.ria.ifzz.RiaApp.repositorie;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileEntityRepository extends CrudRepository<FileEntity, Long> {

    List<FileEntity> findAllByFileName(String fileName);

    @Override
    Iterable<FileEntity> findAll();

    @Override
    Iterable<FileEntity> findAllById(Iterable<Long> longs);

    FileEntity getById(Long id);


}
