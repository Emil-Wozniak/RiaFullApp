package org.ria.ifzz.RiaApp.models;

import cyclops.control.Eval;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Getter
@Setter
public class DataFileMetadata {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private MultipartFile file;
    private final Supplier<List<String>> contents = Eval.later(this::loadContents);

    private List<String> loadContents() {
        try {
            return loadFromFile();
        } catch (IOException ioException) {
            return Collections.singletonList(ioException.getMessage());
        }
    }

    private List<String> loadFromFile() throws IOException {
        List<String> metadata = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            String line;
            InputStream inputStream = file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                metadata.add(line);
            }
        } catch (IOException ioException) {
            LOGGER.warn(ioException.getMessage());
        }
        return metadata;
    }

}
