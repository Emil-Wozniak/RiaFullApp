package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.models.results.ExaminationResult;
import org.ria.ifzz.RiaApp.models.results.RESULT_CLAZZ;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ria.ifzz.RiaApp.services.examination.FileExtractor.*;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getStandardPattern2;
import static org.ria.ifzz.RiaApp.utils.EvenOdd.isOdd;
import static org.ria.ifzz.RiaApp.utils.constants.ControlCurveConstants.*;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CONTROL_CURVE_LENGTH;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CORTISOL_5MIN;

public class FileExtractorImpl<ER extends ExaminationResult> implements FileExtractor, CustomFileReader {

    private ER examinationResult;
    private final CountResultUtil countResultUtil;
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    FileExtractorImpl(ER examinationResult, CountResultUtil countResultUtil) {
        this.examinationResult = examinationResult;
        this.countResultUtil = countResultUtil;
    }

    @SuppressWarnings("unchecked")
    List<ER> generateResults(List<ControlCurve> controlCurves, List<String> metadata) throws ControlCurveException {
        RESULT_CLAZZ clazz = RESULT_CLAZZ.valueOf(examinationResult.getClass().getSimpleName());
        String filename = metadata.get(0).trim();
        String pattern = metadata.get(1).trim();
        setStandardPattern(metadata.get(1));
        metadata = metadata.stream().skip(2).collect(Collectors.toList());

        List<Integer> probeNumbers = setProbeNumber(metadata.size());
        List<String> positions = setPosition(clazz, probeNumbers, pattern);
        List<Integer> CPMs = setCPMs(metadata);

        switch (clazz) {
            case ControlCurve:
                List<Boolean> flags = isFlagged(CPMs);
                controlCurves = createControlCurve(filename, pattern, probeNumbers, positions, CPMs, flags);
                controlCurvesMeterRead(pattern, controlCurves);
                return (List<ER>) controlCurves;
            case ExaminationPoint:
                flags = isFlagged(controlCurves, CPMs);
                List<String> NGs = setNg(controlCurves, CPMs);
                return createExaminationPoints(filename, pattern,
                        probeNumbers.stream().skip(24).collect(Collectors.toList()), positions,
                        CPMs.stream().skip(24).collect(Collectors.toList()),
                        flags.stream().skip(24).collect(Collectors.toList()),
                        NGs.stream().skip(24).collect(Collectors.toList()));

            default:
                throw new ClassCastException();
        }
    }

    //TODO custom exception?
    private void controlCurvesMeterRead(String pattern, List<ControlCurve> controlCurves) throws ControlCurveException {
        try {
            List<Double> meterRead = setMeterRead(pattern, controlCurves);
            for (int i = 0; i < meterRead.size(); i++) {
                double read = meterRead.get(i);
                controlCurves.get(i + 8).setMeterRead(read);
            }
        } catch (ControlCurveException error) {
            LOGGER.error("Couldn't perform meter read, " + error.getMessage());
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
        setAverage(NGs);
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
        switch (clazz) {
            case ControlCurve:
                return probeNumbers.stream().map(pattern_point -> (
                        pattern_point == 1 || pattern_point == 2) ? TOTAL :
                        pattern_point == 3 || pattern_point == 4 || pattern_point == 5 ? NSB :
                                pattern_point == 6 || pattern_point == 7 || pattern_point == 8 ? ZERO
                                        : pattern_point < 23 ? getPattern(pattern, pattern_point) : CONTROL_POINT).collect(Collectors.toList());
            case ExaminationPoint:
                return probeNumbers.stream().skip(24).map(probeNumber -> {
                    int position = probeNumber - CONTROL_CURVE_LENGTH;
                    return isOdd(position) ? (position + 1) / 2 : position / 2;
                }).map(String::valueOf).collect(Collectors.toList());
            default:
                throw new ClassCastException();
        }
    }

    private List<Boolean> isFlagged(List<Integer> CPMs) {
        List<Integer> points = setProbeNumber(CPMs.size());
        List<Boolean> NSB = getZeroOrNsbFlag(CPMs, 2, 4);
        List<Boolean> Zeros = getZeroOrNsbFlag(CPMs, 5, 7);
        List<Boolean> flagged = points.stream().limit(8).map(point -> setFlag(point, NSB, Zeros)).collect(Collectors.toList());
        flagged.addAll(getPatternFlags(CPMs));
        return flagged;
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
        return CPMs.stream().map(point -> flagCondition(point, controlCurves)).collect(Collectors.toList());
    }

    /**
     * sets UtilCounter properties, counts value of ng,
     * and converts result to String
     *
     * @param controlCurves ControlCurve entities
     * @param CPMs          List of integers from CPM metadata column
     */
    private List<String> setNg(List<ControlCurve> controlCurves, List<Integer> CPMs) throws ControlCurveException {
        countResultUtil.createStandardListWithCPMs(controlCurves);
        countResultUtil.setStandardsCpmWithFlags(controlCurves);
        countResultUtil.bindingPercent();
        countResultUtil.logarithmRealZero();
        countResultUtil.countRegressionParameterB();
        countResultUtil.countRegressionParameterA();
        return CPMs.stream()
                .map(point -> countResultUtil.countNg(Double.valueOf(point)))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    private void setStandardPattern(String fileData) {
        if (fileData.equals(CORTISOL_5MIN)) {
            countResultUtil.logDose();
        }
    }

    private List<Double> setMeterRead(String pattern, List<ControlCurve> controlCurves) throws ControlCurveException {
        try {
            countResultUtil.createStandardListWithCPMs(controlCurves);
            countResultUtil.setStandardsCpmWithFlags(controlCurves);
            countResultUtil.bindingPercent();
            countResultUtil.logarithmRealZero();
            countResultUtil.logDose();
            countResultUtil.setCorrelation(getStandardPattern2(pattern));
            countResultUtil.setZeroBindingPercent();
            countResultUtil.countRegressionParameterB();
            countResultUtil.countRegressionParameterA();
            return countResultUtil.countMeterReading();
        } catch (Exception error) {
            LOGGER.error(error.getMessage());
        }
        return new ArrayList<>();
    }

    private List<Double> setAverage(List<String> NGs) {
        List<Double> result = new ArrayList<>();
        double pointA = -1.0, pointB = -1.0;
        for (String ng : NGs) {
            if (pointA == -1.0 && ng != null) {
                pointA = Double.parseDouble(ng);
            } else if (pointB == -1.0 && ng != null) {
                pointB = Double.parseDouble(ng);
            } else {
                double avg = (pointA + pointB ) / 2.0;
                result.add(avg);
                pointA = -1.0;
                pointB = -1.0;
            }
        }
        return result;
    }
}
