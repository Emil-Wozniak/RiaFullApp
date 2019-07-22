package org.ria.ifzz.RiaApp.utils;

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
        if (!streamMetadata.isEmpty()) {
            String filename = streamMetadata.get(0);
            filename = filename.replace(FILENAME_UNNECESSARY_PART, "");
            String hormonePattern = streamMetadata.get(HORMONE_PATTERN);
            hormonePattern = hormonePattern.replace(HORMONE_PATTERN_UNNECESSARY_PART, "");
            examinationResult.add(filename);
            examinationResult.add(hormonePattern);
            for (String metadataLine : streamMetadata) {
                if (!metadataLine.startsWith(DATA_TARGET_POINT)) {
                } else {
                    examinationResult.add(metadataLine);
                }
            }
            return examinationResult;
        }
        return new ArrayList<>();
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
