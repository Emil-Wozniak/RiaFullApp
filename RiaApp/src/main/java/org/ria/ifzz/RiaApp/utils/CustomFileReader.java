package org.ria.ifzz.RiaApp.utils;

import org.jetbrains.annotations.NotNull;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.util.stream.DoubleStream.*;
import static org.ria.ifzz.RiaApp.models.pattern.HormonesPattern.CORTISOL_PATTERN;
import static org.ria.ifzz.RiaApp.utils.constants.DomainConstants.*;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.COLUMN_SPLICER;

/**
 * Provides methods needed to read and return List of String from uploaded file
 */
public interface CustomFileReader {

    /**
     * controlCurves.forEach(System.out::println);
     * takes metadata from uploaded file and
     *
     * @param metadata uploaded file
     * @return Strings containing data for
     */
    static List<String> readFromStream(DataFileMetadata metadata) throws IOException {
        List<String> streamMetadata = metadata.getContents().get();
        List<String> examinationResult = new ArrayList<>();
        addFilenameAndPattern(examinationResult, streamMetadata);
        List<String> results = streamMetadata.stream().map(CustomFileReader::isDataResult).collect(Collectors.toList());
        removeEmpty(results);
        examinationResult.addAll(results);
        return examinationResult;
    }

    /**
     * @param line line of String from uploaded file
     * @return lines which contain examination data, starts with 'Unk'
     */
    static String isDataResult(String line) {
        return (!line.startsWith(DATA_TARGET_POINT)) ? "" : line;
    }

    /**
     * method removes null or blank lines from metadata
     *
     * @param strings pre-formatted lines of String from uploaded file
     */
    static void removeEmpty(List<String> strings) {
        strings.removeIf(s -> s == null || s.isEmpty());
    }

    /**
     * @param examinationResult pre-formatted lines of String from uploaded file
     * @param streamMetadata    list of string containing examination results,
     *                          but first and second values are filename and hormone pattern
     */
    static void addFilenameAndPattern(List<String> examinationResult, List<String> streamMetadata) {
        String filename = getCleanFileName(streamMetadata);
        String hormonePattern = getPatternFromMetadata(streamMetadata);
        examinationResult.add(filename);
        examinationResult.add(hormonePattern);
    }

    @NotNull
    static String getPatternFromMetadata(List<String> streamMetadata) {
        return streamMetadata.get(4).replace(HORMONE_PATTERN_UNNECESSARY_PART,"");
    }

    @NotNull
    static String getCleanFileName(List<String> streamMetadata) {
        return streamMetadata.get(0).replace(FILENAME_UNNECESSARY_PART, "");
    }

    static String getMatchingCPMsString(String line) {
        int columnNumber = 3;
        List<String> wordInLine = Arrays.asList(line.split(COLUMN_SPLICER));
        return wordInLine.get(columnNumber);
    }

    static List<Double> getStandardPattern(List<String> metadata) {
        List<Double> hormonePattern = new ArrayList<>();
        if (metadata.get(1).trim().equals("KORTYZOL_5_MIN")) {
            hormonePattern = of(CORTISOL_PATTERN).boxed().collect(Collectors.toCollection(ArrayList::new));
        }
        return hormonePattern;
    }

    static List<Double> getStandardPattern2(String pattern) {
        List<Double> hormonePattern = new ArrayList<>();
        if (pattern.equals("KORTYZOL_5_MIN")) {
            hormonePattern = of(CORTISOL_PATTERN).boxed().collect(Collectors.toCollection(ArrayList::new));
        }
        return hormonePattern;
    }
}
