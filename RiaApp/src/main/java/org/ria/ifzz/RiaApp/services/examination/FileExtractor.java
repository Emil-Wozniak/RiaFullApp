package org.ria.ifzz.RiaApp.services.examination;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.ria.ifzz.RiaApp.models.pattern.HormonesPattern.CORTISOL_PATTERN;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getMatchingString;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CORTISOL_5MIN;

public interface FileExtractor {

    /**
     * generates List of integers
     *
     * @param to metadata size
     * @return List of integers
     */
    static List<Integer> setProbeNumber(int to) {
        return IntStream.rangeClosed(1, to).boxed().collect(Collectors.toList());
    }

    /**
     * takes List of String with data from uploaded file
     *
     * @param metadata data from uploaded file
     * @return List containing CPM values as Strings
     */
    static List<Integer> setCPMs(List<String> metadata) {
        return metadata.stream().map(line -> {
            line = getMatchingString(line, 3);
            return line;
        }).map(Integer::parseInt).collect(Collectors.toList());
    }

    /**
     * @param pattern       represents hormones patterns which should be founded in uploaded file
     * @param pattern_point point in a pattern
     * @return explicit pattern points by detected pattern
     */
    static String getPattern(String pattern, int pattern_point) {
        if (pattern.equals(CORTISOL_5MIN)) {
            double point = CORTISOL_PATTERN[pattern_point - 8];
            return String.valueOf(point);
        }
        return "";
    }
}
