package org.ria.ifzz.RiaApp.domain;

import lombok.Getter;
import lombok.Setter;
import org.ria.ifzz.RiaApp.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Supplier;

@Getter
@Setter
public class FileModel {

    private Long customerId;
    private MultipartFile file;

    private Supplier<String> contents = this::loadContents;

    private String loadContents() {
        try {
            return loadFromFile();
        } catch (IOException ioException) {
            throw new StorageException("Storage Failed " + ioException.getMessage()); //DataFileUnavailableException(ioException);
        }
    }

    private String loadFromFile() throws IOException {
        return new String(file.getBytes());
    }

}
