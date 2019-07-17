package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.User;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.exception.StorageException;
import org.ria.ifzz.RiaApp.repository.FileEntityRepository;
import org.ria.ifzz.RiaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.ria.ifzz.RiaApp.service.FileNameChecker.isFileAlreadyStorage;
import static org.ria.ifzz.RiaApp.service.FileNameChecker.isWrongFileName;

@RestController
public class FileEntityStorageService implements StorageService {

    private final Path rootLocation;
    private final FileEntityRepository fileEntityRepository;
    private final BacklogService backlogService;
    private final UserRepository userRepository;

    @Autowired
    public FileEntityStorageService(StorageProperties properties, FileEntityRepository fileEntityRepository, BacklogService backlogService, UserRepository userRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileEntityRepository = fileEntityRepository;
        this.backlogService = backlogService;
        this.userRepository = userRepository;
    }

    /**
     * @param file     uploaded file
     * @param username user credentials
     * @return file_entity object with all necessary contents
     * @throws IOException if upload is not successful
     */
    @Override
    public FileEntity storeAndSaveFileEntity(MultipartFile file, String username) throws IOException, StorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        isFileAlreadyStorage(fileEntityRepository, filename);
        isWrongFileName(filename);

        try {
            User user = userRepository.findByUsername(username);
            FileEntity fileEntity = new FileEntity(file.getOriginalFilename(), file.getContentType(), filename, user.getUsername(), user);
            if (fileEntity.getId() == null) {
                fileEntity.setBacklog(backlogService.setBacklog(fileEntity));
            }
            return fileEntityRepository.save(fileEntity);
        } catch (Exception exception) {
            throw new StorageException("File already uploaded: " + filename);
        }
    }

    @Override
    public Iterable<FileEntity> loadAll() {
        return fileEntityRepository.findAll();
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public FileEntity getByDataId(String dataId) throws FileEntityNotFoundException {
        FileEntity fileEntity = fileEntityRepository.getByDataId(dataId);
        isFileEntityNull(dataId, fileEntity);
        return fileEntity;
    }

    private void isFileEntityNull(String dataId, FileEntity fileEntity) throws FileEntityNotFoundException {
        if (fileEntity == null) {
            throw new FileEntityNotFoundException("File does not exist" + dataId);
        }
    }

    /**
     * Generate directory for uploaded files
     */
    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException exception) {
            throw new StorageException("Could not initialize storage");
        }
    }
}
