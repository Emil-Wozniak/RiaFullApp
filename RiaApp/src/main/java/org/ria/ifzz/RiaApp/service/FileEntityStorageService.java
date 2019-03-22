package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.User;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.exception.StorageException;
import org.ria.ifzz.RiaApp.exception.StorageFileNotFoundException;
import org.ria.ifzz.RiaApp.repository.FileEntityRepository;
import org.ria.ifzz.RiaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
public class FileEntityStorageService implements StorageService {

    private final Path rootLocation;
    private final FileEntityRepository fileEntityRepository;
    private final BacklogService backlogService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public FileEntityStorageService(StorageProperties properties, FileEntityRepository fileEntityRepository, BacklogService backlogService, UserRepository userRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileEntityRepository = fileEntityRepository;
        this.backlogService = backlogService;
        this.userRepository = userRepository;
    }

    /**
     *
     * @param file uploaded
     * @param redirectAttributes page response
     * @param username user credentials
     * @param setId file id
     * @return file_entity object with all necessary contents
     * @throws IOException if upload is not successful
     */
    @Override
    public FileEntity storeAndSaveFileEntity(MultipartFile file, RedirectAttributes redirectAttributes, String username, int setId) throws IOException {
        FileEntity fileEntity = new FileEntity(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes());

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileEntityRepository.findByFileName(filename) != null){
                throw new StorageException("File already uploaded: " + filename);
            }
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
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename);
        }
        try{
            User user = userRepository.findByUsername(username);
            fileEntity.setUser(user);
            fileEntity.setFileOwner(user.getUsername());
            fileEntity.setDataId(fileEntity.getFileName() + "_" + setId);
            if (fileEntity.getId() == null){
                fileEntity.setBacklog(backlogService.setBacklog(fileEntity));
            }
           return fileEntityRepository.save(fileEntity);
        } catch (Exception exception){
            throw new StorageException("File already uploaded: " + filename);
        }
    }

    @Override
    public Iterable<FileEntity> loadAll() {
        return fileEntityRepository.findAll();
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
            throw new StorageFileNotFoundException("Could not read file: " + filename);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public FileEntity getByDataId(String dataId) throws FileEntityNotFoundException {
        FileEntity fileEntity = fileEntityRepository.getByDataId(dataId);
        if (fileEntity == null) {
            throw new FileEntityNotFoundException(
                    "File does not exist" + dataId);
        }
        return fileEntity;
    }

    @Override
    public void delete(Long id) {
        FileEntity fileEntity = fileEntityRepository.getById(id);
        fileEntityRepository.delete(fileEntity);
    }

    /**
     * Generate directory for uploaded files
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException exception) {
            throw new StorageException("Could not initialize storage");
        }
    }
}
