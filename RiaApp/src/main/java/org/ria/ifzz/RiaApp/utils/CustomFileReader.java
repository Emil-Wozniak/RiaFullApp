package org.ria.ifzz.RiaApp.utils;

import lombok.Getter;
import org.ria.ifzz.RiaApp.domain.DataFileMetadata;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.DomainConstants.*;

/**
 * Provides methods needed to read and return List of String from uploaded file
 */
public interface CustomFileReader {

    /**
     * takes metadata from uploaded file and
     * @param metadata uploaded file
     * @return Strings containing data for
     * @throws IOException
     */
    static List<String> readFromStream(DataFileMetadata metadata) throws IOException {
        List<String> examinationResult = new ArrayList<>();
        List<String> streamMetadata = metadata.getContents().get();
        String hormonePattern = streamMetadata.get(HORMONE_PATTERN);
        hormonePattern = hormonePattern.replace(HORMONE_PATTERN_UNNECESSARY_PART, "");
        examinationResult.add(hormonePattern);
        for (String metadataLine : streamMetadata) {
            if (!metadataLine.startsWith(DATA_TARGET_POINT)) {
            } else {
                examinationResult.add(metadataLine);
            }
        }
        examinationResult.forEach(System.out::println);
        return examinationResult;
    }

    /**
     * Finds the all word in all entries in the list that matches with the column number
     *
     * @param list         The list of strings to check
     * @param columnNumber The targeted column to use
     * @return list containing the words of all matching entries
     */
    static List<String> getMatchingStrings(List<String> list, Integer columnNumber) {

        List<String> matches = new ArrayList<>();
        for (String added : list) {
            List<String> wordInLine = Arrays.asList(added.split("\\t"));
            if (wordInLine.size() == 5) {
                if (!wordInLine.isEmpty()) {
                    matches.add(wordInLine.get(columnNumber));
                }
            } else {
                matches.isEmpty();
            }
        }
        return matches;
    }
}
