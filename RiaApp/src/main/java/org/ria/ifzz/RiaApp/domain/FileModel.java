package org.ria.ifzz.RiaApp.domain;

import cyclops.control.Eval;
import lombok.Getter;
import lombok.Setter;
import org.ria.ifzz.RiaApp.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Getter
@Setter
public class FileModel {

    private Long customerId;
    private MultipartFile file;
    private String fileName;

    private Supplier<List<String>> contents = Eval.later(this::loadContents);

    private List<String> loadContents() {
        try {
            return loadFromFile();
        } catch (IOException ioException) {
            throw new StorageException("Storage Failed " + ioException.getMessage());
        }
    }

    private List<String> loadFromFile() throws IOException {
        BufferedReader bufferedReader;
        List<String> result = new ArrayList<>();
        try {
            String line;
            InputStream inputStream = file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        fileName = result.get(0).replace("C:\\mbw\\results\\","");
        System.out.println(fileName);
        return result;
    }

}
