package org.ria.ifzz.RiaApp.utils;

import lombok.Getter;
import org.ria.ifzz.RiaApp.domain.DataFileMetadata;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides methods needed to read and return List of String from uploaded file
 */
@Service
public class CustomFileReader {

    @Getter
    private String uploadComment = "File content:\n";
    @Getter
    private String positionRegex = "[\\w]";

    //TODO docs are necessary
    public List<String> readFromStream(DataFileMetadata file) throws IOException {
        List<String> fileManager = file.getContents().get();
        List<String> lines = new ArrayList<>();
        String pattern = fileManager.get(4);
        pattern = pattern.replace("Name: COPY_OF_H-3_", "");
        lines.add(pattern);
        for (String line : fileManager) {
            if (!line.startsWith(" \tUnk")) {
            } else {
                lines.add(line);
            }
        }
        lines.forEach(System.out::println);
        return lines;
    }

    /**
     * Finds the all word in all entries in the list that matches with the column number
     *
     * @param list         The list of strings to check
     * @param columnNumber The targeted column to use
     * @return list containing the words of all matching entries
     */
    public List<String> getMatchingStrings(List<String> list, Integer columnNumber) {

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
//        matches.remove(0);
        return matches;
    }

    public List<String> getIndex(List<String> list, Integer columnNumber) {
        List<String> matches = getMatchingStrings(list, columnNumber);
        return matches;
    }
}
