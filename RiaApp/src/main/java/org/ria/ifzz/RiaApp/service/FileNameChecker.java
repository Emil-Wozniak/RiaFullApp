package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.exception.StorageException;
import org.ria.ifzz.RiaApp.repository.FileEntityRepository;

public interface FileNameChecker {
    static void isWrongFileName(String filename) throws StorageException {
        if (filename.contains("..")) {
            throw new StorageException("Cannot store file with relative path outside current directory " + filename);
        }
    }

    static void isFileAlreadyStorage(FileEntityRepository fileEntityRepository, String filename) throws StorageException {
        if (fileEntityRepository.findByFileName(filename) != null) {
            throw new StorageException("File already uploaded: " + filename);
        }
    }
}
