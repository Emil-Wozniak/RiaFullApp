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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.ria.ifzz.RiaApp.utils.constants.DomainConstants.*;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.COLUMN_SPLICER;

class CustomFileReaderTest {

    private DataFileMetadata metadata;

    @BeforeEach
    void setUp() throws IOException {
        metadata = getFileContents();
    }

    private DataFileMetadata getFileContents() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("example/A16_244.txt")).getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        return new DataFileMetadata(multipartFile);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readFromStream() {
        List<String> streamMetadata = metadata.getContents().get();
        List<String> examinationResult = new ArrayList<>();
        CustomFileReader.addFilenameAndPattern(examinationResult, streamMetadata);
        List<String> results = streamMetadata.stream().map(CustomFileReader::isDataResult).collect(Collectors.toList());
        CustomFileReader.removeEmpty(results);
        examinationResult.addAll(results);
        assertEquals(627, streamMetadata.size());
    }

    @Test
    void isDataResult_positive() {
        List<String> streamMetadata = metadata.getContents().get();
        assertTrue(streamMetadata.get(20).startsWith(DATA_TARGET_POINT));
    }

    @Test
    void isDataResult_negative() {
        List<String> streamMetadata = metadata.getContents().get();
        assertFalse(streamMetadata.get(5).startsWith(DATA_TARGET_POINT));
    }

    @Test
    void removeEmpty() {
        List<String> streamMetadata = metadata.getContents().get();
        streamMetadata = streamMetadata.stream().map(CustomFileReader::isDataResult).collect(Collectors.toList());
        streamMetadata.removeIf(s -> s == null || s.isEmpty());
        assertEquals(384, streamMetadata.size());
    }

    @Test
    void addFilenameAndPattern() {
        List<String> streamMetadata = metadata.getContents().get();
        String filename = CustomFileReader.getCleanFileName(streamMetadata);
        String pattern = CustomFileReader.getPatternFromMetadata(streamMetadata);
        assertEquals("A16_244.txt", filename);
        assertEquals("KORTYZOL_5_MIN", pattern);
    }

    @Test
    void getPatternFromMetadata() {
        List<String> streamMetadata = metadata.getContents().get();
        String hormonePattern = streamMetadata.get(4).replace(HORMONE_PATTERN_UNNECESSARY_PART, "");
        assertEquals("KORTYZOL_5_MIN", hormonePattern);
    }

    @Test
    void getCleanFileName() {
        List<String> streamMetadata = metadata.getContents().get();
        String filename = streamMetadata.get(0).replace(FILENAME_UNNECESSARY_PART, "");
        assertEquals("A16_244.txt", filename);
    }

    @Test
    void getMatchingString() throws IOException {
        List<String> fileContent = CustomFileReader.readFromStream(metadata);
        String testLine = fileContent.get(3);
        List<String> wordInLine = Arrays.asList(testLine.split(COLUMN_SPLICER));
        assertEquals("1982", wordInLine.get(3));
    }
}
