package org.ria.ifzz.RiaApp.utils;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides methods needed to read and return List of String from uploaded file
 */
@Service
public class CustomFileReader {

    @Getter
    private String uploadComment = "File content:\n========================================================";
    @Getter
    private String positionRegex = "[\\w]";

    /**
     * reads file from "/upload" directory, skips first 13 lines and set maximum limit of expected lines;
     * @param file is upload to "/upload" directory
     * @return list of String if file is not empty
     * @throws IOException
     */
    public List<String> readStoredTxtFile(MultipartFile file) throws IOException {
        List<String> list;
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get("upload-dir" + "/" + file.getOriginalFilename()))) {
            list = reader.lines()
                    .skip(13)
                    .limit(500)
                    .collect(Collectors.toList());
        }
        return list;
    }

    /**
     * takes list of Strings from files stored in "/upload" directory
     * and return lists of Strings which starts with char 'U';
     * @param list comes from reading file;
     * @return list of String if fulfills requirements, should include
     * only lines which doesn't start with not expected letters,
     * otherwise return exception
     */
    public List<String> cleanStoredTxtFile(List<String> list) {
        try {
            list.stream().filter(line->!line.startsWith("U")).collect(Collectors.toList());
            list.removeIf(line -> line.startsWith("P"));
            list.removeIf(line -> line.startsWith("C"));
            list.removeIf(line -> line.startsWith("A"));
            list.removeIf(line -> line.startsWith("E"));
            list.removeIf(line -> line.startsWith("T"));
            list.removeIf(line -> line.startsWith("="));
            list.removeIf(line -> line.startsWith("B"));
            list.removeIf(line -> line.startsWith("D"));
            list.removeIf(line -> line.startsWith(" \t1"));
            list.removeAll(Collections.singleton(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.forEach(System.out::println);
        return list;
    }

    /**
     * Finds the all word in all entries in the list that matches with the column number
     * @param list The list of strings to check
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
        matches.remove(0);
        return matches;
    }

    public List<String> getIndex(List<String> list, Integer columnNumber) {
        List<String> matches = getMatchingStrings(list, columnNumber);
        return matches;
    }
}
