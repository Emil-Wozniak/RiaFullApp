package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.models.results.ExaminationResult;
import org.ria.ifzz.RiaApp.models.results.RESULT_CLAZZ;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ria.ifzz.RiaApp.services.examination.FileExtractor.*;
import static org.ria.ifzz.RiaApp.services.strategies.SpreadCounter.isSpread;
import static org.ria.ifzz.RiaApp.utils.EvenOdd.isOdd;
import static org.ria.ifzz.RiaApp.utils.constants.ControlCurveConstants.*;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CONTROL_CURVE_LENGTH;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CORTISOL_5MIN;

public class FileExtractorImpl<ER extends ExaminationResult> implements FileExtractor, CustomFileReader {

    private ER examinationResult;
    private final CountResultUtil countResultUtil;

    FileExtractorImpl(ER examinationResult, CountResultUtil countResultUtil) {
        this.examinationResult = examinationResult;
        this.countResultUtil = countResultUtil;
    }

    @SuppressWarnings("unchecked")
    List<ER> generateResults(List<ControlCurve> controlCurves, List<String> metadata) {
        RESULT_CLAZZ clazz = RESULT_CLAZZ.valueOf(examinationResult.getClass().getSimpleName());
        String filename = metadata.get(0);
        String pattern = metadata.get(1);
        setStandardPattern(metadata.get(1));
        metadata = metadata.stream().skip(2).collect(Collectors.toList());

        List<Integer> probeNumbers = setProbeNumber(metadata.size());
        List<String> positions = setPosition(clazz, probeNumbers, pattern);
        List<Integer> CPMs = setCPMs(metadata);
        List<Boolean> flags = isFlagged(CPMs);

        switch (clazz) {
            case ControlCurve:
                controlCurves = createControlCurve(filename, pattern, probeNumbers, positions, CPMs, flags);
                return (List<ER>) controlCurves;
            case ExaminationPoint:
                flags = isFlagged(controlCurves, CPMs);
                List<String> NGs = setNg(controlCurves, CPMs);
                return createExaminationPoints(filename, pattern, probeNumbers, positions, CPMs, flags, NGs);
            default:
                return new ArrayList<>();
        }
    }

    private List<ControlCurve> createControlCurve(String filename, String pattern, List<Integer> probeNumbers, List<String> positions, List<Integer> CPMs, List<Boolean> flags) {
        List<ControlCurve> controlCurves = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ControlCurve controlCurvePoint = new ControlCurve(filename, pattern, probeNumbers.get(i), positions.get(i), CPMs.get(i), flags.get(i));
            controlCurves.add(controlCurvePoint);
        }
        return controlCurves;
    }

    @SuppressWarnings("unchecked")
    private List<ER> createExaminationPoints(String filename, String pattern, List<Integer> probeNumbers, List<String> positions, List<Integer> CPMs, List<Boolean> flags, List<String> NGs) {
        List<ExaminationPoint> examinationPoints = new ArrayList<>();
        for (int i = 0; i < probeNumbers.size(); i++) {
            ExaminationPoint examinationPoint = new ExaminationPoint(filename, pattern, probeNumbers.get(i), positions.get(i), CPMs.get(i), flags.get(i), NGs.get(i));
            examinationPoints.add(examinationPoint);
        }
        return (List<ER>) examinationPoints;
    }

    /**
     * generates pairs of the same integers
     *
     * @param clazz        generic type of generated list
     * @param probeNumbers generated List of integers
     * @param pattern      represents hormones patterns which should be founded in uploaded file
     * @return List of integers pair
     */
    private List<String> setPosition(RESULT_CLAZZ clazz, List<Integer> probeNumbers, String pattern) {
        List<String> positions = new ArrayList<>();
        switch (clazz) {
            case ExaminationPoint:
                for (Integer probeNumber : probeNumbers) {
                    int position = probeNumber - CONTROL_CURVE_LENGTH;
                    if (isOdd(position)) {
                        position = (position + 1) / 2;
                    } else {
                        position = position / 2;
                    }
                    positions.add(String.valueOf(position));
                }
                return positions;
            case ControlCurve:
                for (int pattern_point = 0; pattern_point < 24; pattern_point++) {
                    if (pattern_point == 0 || pattern_point == 1) {
                        positions.add(TOTAL);
                    } else if (pattern_point == 2 || pattern_point == 3 || pattern_point == 4) {
                        positions.add(NSB);
                    } else if (pattern_point == 5 || pattern_point == 6 || pattern_point == 7) {
                        positions.add(ZERO);
                    } else if (pattern_point < 22) {
                        getPattern(pattern, pattern_point, positions);
                    } else {
                        positions.add(CONTROL_POINT);
                    }
                }
                return positions;
            default:
                return positions;
        }
    }

    /**
     * takes integers from CPMs List and checks if value is more than Control Curve zeros properties,
     * or less than Control Curve NSBs properties.
     *
     * @param controlCurves ControlCurve entities
     * @param CPMs          values from cpm table
     * @return true if cpm more than NSBs or less than Zeros, and false in another way
     */
    private List<Boolean> isFlagged(List<ControlCurve> controlCurves, List<Integer> CPMs) {
        List<Boolean> flagged = new ArrayList<>();
        double nsb1 = controlCurves.get(5).getCpm(), nsb2 = controlCurves.get(6).getCpm(), nsb3 = controlCurves.get(7).getCpm();
        for (Integer cpm : CPMs) {
            if (cpm > nsb1 || cpm > nsb2 || cpm > nsb3) {
                flagged.add(true);
            } else if (cpm < controlCurves.get(2).getCpm() || cpm < controlCurves.get(3).getCpm() || cpm < controlCurves.get(4).getCpm()) {
                flagged.add(true);
            } else {
                flagged.add(false);
            }
        }
        return flagged;
    }

    private List<Boolean> isFlagged(List<Integer> CPMs) {
        List<Boolean> flagged = new ArrayList<>();
        List<Boolean> NSB = getZeroOrNsb(CPMs, 2, 4);
        List<Boolean> Zeros = getZeroOrNsb(CPMs, 5, 7);
        double nsb1 = CPMs.get(5), nsb2 = CPMs.get(6), nsb3 = CPMs.get(7);
        flagged.add(false);
        flagged.add(false);
        flagged.addAll(NSB);
        flagged.addAll(Zeros);
        for (int element = 8; element < CPMs.size(); element++) {
            if (CPMs.get(element) > nsb1 || nsb2 > CPMs.get(element) || CPMs.get(element) > nsb3) {
                flagged.add(true);
            } else {
                flagged.add(false);
            }
        }
        return flagged;
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
    private List<Boolean> getZeroOrNsb(List<Integer> CPMs, int from, int to) {
        List<Integer> zeroOrNsbPoints = CPMs.stream().skip(from).limit(to).collect(Collectors.toList());
        return checkNSBsOrZeros(zeroOrNsbPoints);
    }

    private List<Boolean> checkNSBsOrZeros(List<Integer> CPMs) {
        return isSpread(CPMs);
    }

    /**
     * sets UtilCounter properties, counts value of ng,
     * and converts result to String
     *
     * @param controlCurves ControlCurve entities
     * @param CPMs          List of integers from CPM metadata column
     */
    private List<String> setNg(List<ControlCurve> controlCurves, List<Integer> CPMs) {
        countResultUtil.createStandardListWithCPMs(controlCurves);
        countResultUtil.setStandardsCpmWithFlags(controlCurves);
        List<Double> binding = countResultUtil.bindingPercent();
        countResultUtil.logarithmRealZero(binding);
        countResultUtil.countRegressionParameterB();
        countResultUtil.countRegressionParameterA();
        return CPMs.stream().map(point -> countResultUtil.countNg(Double.valueOf(point))).map(String::valueOf).collect(Collectors.toList());
    }

    private void setStandardPattern(String fileData) {
        if (fileData.equals(CORTISOL_5MIN)) {
            countResultUtil.logDose();
        }
    }
}
