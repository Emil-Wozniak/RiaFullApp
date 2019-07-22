package org.ria.ifzz.RiaApp.utils;

import org.jetbrains.annotations.NotNull;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ria.ifzz.RiaApp.utils.constants.DomainConstants.*;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.COLUMN_SPLICER;

/**
 * Provides methods needed to read and return List of String from uploaded file
 */
public interface CustomFileReader {

    /**
     * takes metadata from uploaded file and
     *
     * @param metadata uploaded file
     * @return Strings containing data for
     */
    static List<String> readFromStream(DataFileMetadata metadata) throws IOException {
        List<String> examinationResult = new ArrayList<>();
        List<String> streamMetadata = metadata.getContents().get();
        addExactedDataToResults(examinationResult, streamMetadata);
        for (String metadataLine : streamMetadata) {
            isDataResult(examinationResult, metadataLine);
        }
        return examinationResult;
    }

    static void isDataResult(List<String> examinationResult, String metadataLine) {
        if (!metadataLine.startsWith(DATA_TARGET_POINT)) {
        } else {
            examinationResult.add(metadataLine);
        }
    }

    static void removeEmpty(List<String> strings){
        strings.removeIf(s -> s == null || s.isEmpty());
    }

    static void addExactedDataToResults(List<String> examinationResult, List<String> streamMetadata) {
        String filename = getCleanFileName(streamMetadata, 0, FILENAME_UNNECESSARY_PART);
        String hormonePattern = getPatternFromMetadata(streamMetadata);
        examinationResult.add(filename);
        examinationResult.add(hormonePattern);
    }

    @NotNull
    static String getPatternFromMetadata(List<String> streamMetadata) {
        String hormonePattern = streamMetadata.get(HORMONE_PATTERN);
        hormonePattern = hormonePattern.replace(HORMONE_PATTERN_UNNECESSARY_PART, "");
        return hormonePattern;
    }

    @NotNull
    static String getCleanFileName(List<String> streamMetadata, int i, String filenameUnnecessaryPart) {
        String filename = streamMetadata.get(i);
        filename = filename.replace(filenameUnnecessaryPart, "");
        return filename;
    }

    static String getMatchingString(String line, Integer columnNumber) {
        String matchesLine = "";
        List<String> wordInLine = Arrays.asList(line.split(COLUMN_SPLICER));
        if (!wordInLine.isEmpty()) {
            matchesLine = wordInLine.get(columnNumber);
        }
        return matchesLine;
    }
}
