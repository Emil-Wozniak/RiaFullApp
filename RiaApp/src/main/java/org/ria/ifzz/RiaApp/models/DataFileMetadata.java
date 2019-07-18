package org.ria.ifzz.RiaApp.models;

import cyclops.control.Eval;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.ria.ifzz.RiaApp.models.DomainConstants.FILENAME_LINE;
import static org.ria.ifzz.RiaApp.models.DomainConstants.FILENAME_UNNECESSARY_PART;

@Getter
@Setter
public class DataFileMetadata {

    private Long customerId;
    private MultipartFile file;
    private String fileName;
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
            System.err.println(ioException.getMessage());
        }
        this.fileName = metadata.get(FILENAME_LINE).replace(FILENAME_UNNECESSARY_PART, "");
        return metadata;
    }

}
