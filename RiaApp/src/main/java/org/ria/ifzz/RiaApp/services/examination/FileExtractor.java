package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.ria.ifzz.RiaApp.models.pattern.HormonesPattern.CORTISOL_PATTERN;
import static org.ria.ifzz.RiaApp.services.strategies.SpreadCounter.isSpread;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getMatchingCPMsString;
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
        return metadata.stream()
                .map(CustomFileReader::getMatchingCPMsString)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * @param pattern       represents hormones patterns which should be founded in uploaded file
     * @param pattern_point point in a pattern
     * @return explicit pattern points by detected pattern
     */
    static String getPattern(String pattern, int pattern_point) {
        if (pattern.equals(CORTISOL_5MIN)) {
            double point = CORTISOL_PATTERN[pattern_point - 9];
            return String.valueOf(point);
        }
        return "";
    }

    static Boolean setFlag(int point, List<Boolean> NSB, List<Boolean> Zeros) {
        switch (point) {
            case 1:
            case 2:
                return false;
            case 3:
            case 4:
            case 5:
                return NSB.get(setPoint(point, 3));
            case 6:
            case 7:
            case 8:
                return Zeros.get(setPoint(point, 6));
            default:
                throw new IllegalStateException("Unexpected value: " + point);
        }
    }

    static int setPoint(int position, int value) {
        return position - value;
    }

    /**
     * {@code CPMs} is a list of double value, which elements will be check,
     * to define the difference. If difference between one element and another
     * is more than 10%, element return true, in another way return false;
     *
     * @param CPMs List of elements which element will be check
     * @param from first element which will be checking
     * @param to   last element which will be checking
     * @return list of boolean value for all checking elements
     */
    static List<Boolean> getZeroOrNsbFlag(List<Integer> CPMs, int from, int to) {
        List<Integer> zeroOrNsbPoints = CPMs.stream().skip(from).limit(to).collect(Collectors.toList());
        return checkNSBsOrZeros(zeroOrNsbPoints);
    }

    static List<Boolean> checkNSBsOrZeros(List<Integer> CPMs) {
        return isSpread(CPMs);
    }

    static List<Boolean> getPatternFlags(List<Integer> CPMs) {
        List<Boolean> flagged = new ArrayList<>();
        double nsb1 = CPMs.get(5), nsb2 = CPMs.get(6), nsb3 = CPMs.get(7);
        for (int element = 8; element < CPMs.size(); element++) {
            int evalPoint = CPMs.get(element);
            if (evalPoint > nsb1 || evalPoint > nsb2 || evalPoint > nsb3) {
                flagged.add(true);
            } else {
                flagged.add(false);
            }
        }
        return flagged;
    }

    static boolean flagCondition(int cpm, List<ControlCurve> controlCurves) {
        return (cpm < controlCurves.get(2).getCpm() || cpm < controlCurves.get(3).getCpm() || cpm < controlCurves.get(4).getCpm()) || (cpm > controlCurves.get(5).getCpm() || cpm > controlCurves.get(6).getCpm() || cpm > controlCurves.get(7).getCpm());
    }
}
