package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.exceptions.StorageException;
import org.ria.ifzz.RiaApp.exceptions.StorageFileNotFoundException;
import org.ria.ifzz.RiaApp.repositories.FileDataRepository;
import org.ria.ifzz.RiaApp.repositories.FileEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final FileEntityRepository fileEntityRepository;
    private final FileDataRepository fileDataRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, FileEntityRepository fileEntityRepository, FileDataRepository fileDataRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileEntityRepository = fileEntityRepository;
        this.fileDataRepository = fileDataRepository;
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Iterable<FileEntity> loadAll() {
        return fileEntityRepository.findAll();
//                    Files.walk(this.rootLocation, 1)
//                .filter(path -> !path.equals(this.rootLocation))
//                .map(this.rootLocation::relativize);
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public FileData getById(Long id) throws FileNotFoundException {
        FileData fileData = fileDataRepository.getById(id);
        if (fileData == null) {
            throw new FileNotFoundException(
                    "Project does not exist");
        }
        return fileData;
    }

    @Override
    public Iterable<FileEntity> findAllFile() {
        return fileEntityRepository.findAll();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException exception) {
            throw new StorageException("Could not initialize storage", exception);
        }
    }
}
