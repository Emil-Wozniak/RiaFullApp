package org.ria.ifzz.RiaApp.utils;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.ria.ifzz.RiaApp.utils.constants.DomainConstants.DATA_TARGET_POINT;

class CustomFileReaderTest {

    private DataFileMetadata metadata;

    @BeforeEach
    void setUp() throws IOException {
        metadata = getFileContents();
    }

    private DataFileMetadata getFileContents() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("example/A16_244.txt").getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        return new DataFileMetadata(multipartFile);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readFromStream() {
        List<String> streamMetadata = metadata.getContents().get();
        List<String> examinationResult = new ArrayList<>();
        CustomFileReader.addFilenameAndPattern(examinationResult,streamMetadata);
        List<String> results = streamMetadata.stream().map(this::isDataResult).collect(Collectors.toList());
        removeEmpty(results);
        examinationResult.addAll(results);
        assertEquals(627,streamMetadata.size());
    }

    private String isDataResult(String metadataLine) {
        return (!metadataLine.startsWith(DATA_TARGET_POINT)) ? "" : metadataLine;
    }

    static void removeEmpty(List<String> strings){
        strings.removeIf(s -> s == null || s.isEmpty());
    }

    @Test
    void getMatchingString() {
    }
}
