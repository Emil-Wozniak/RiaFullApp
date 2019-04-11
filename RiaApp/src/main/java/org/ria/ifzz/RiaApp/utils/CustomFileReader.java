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
@Service
public class CustomFileReader {

    private List<String> examinationResult = new ArrayList<>();

    @Getter
    private String uploadComment = "File content:\n";
    @Getter
    private String positionRegex = "[\\w]";

    /**
     * takes metadata from uploaded file and
     *
     * @param data uploaded file
     * @return Strings containing data for
     * @throws IOException
     */
    public List<String> readFromStream(DataFileMetadata data) throws IOException {
        List<String> streamMetadata = data.getContents().get();
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
     * @param data             The list of strings to check
     * @param columnNumberInData The targeted column to use
     * @return list containing the words of all matching entries
     */
    public List<String> getMatchingStrings(List<String> data, Integer columnNumberInData) {
        List<String> matches = new ArrayList<>();
        for (String matchData : data) {
            List<String> dataInLine = Arrays.asList(matchData.split("\\t"));
            if (dataInLine.size() == 5) {
                matches.add(dataInLine.get(columnNumberInData));
            } else {
                matches.isEmpty();
            }
        }
        return matches;
    }

    public List<String> getIndex(List<String> list, Integer columnNumber) {
        List<String> matches = getMatchingStrings(list, columnNumber);
        return matches;
    }
}
