package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.models.results.ExaminationResult;
import org.ria.ifzz.RiaApp.models.results.RESULT_CLAZZ;
import org.ria.ifzz.RiaApp.utils.counter.Counter;
import org.ria.ifzz.RiaApp.utils.reader.CustomFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ria.ifzz.RiaApp.services.examination.FileExtractor.*;
import static org.ria.ifzz.RiaApp.utils.constants.ControlCurveConstants.*;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CONTROL_CURVE_LENGTH;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CORTISOL_5MIN;
import static org.ria.ifzz.RiaApp.utils.constants.ExtractorConstants.*;
import static org.ria.ifzz.RiaApp.utils.reader.CustomFileReader.getStandardPattern2;

public class FileExtractorImpl<ER extends ExaminationResult> implements FileExtractor, CustomFileReader {

    private ER examinationResult;
    private Counter counter;
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    FileExtractorImpl(ER examinationResult, Counter counter) {
        this.examinationResult = examinationResult;
        this.counter = counter;
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
        List<Double> averages = getAverage(NGs);
        for (int i = 0; i < probeNumbers.size(); i++) {
            ExaminationPoint examinationPoint = new ExaminationPoint(filename, pattern, probeNumbers.get(i), positions.get(i), CPMs.get(i), flags.get(i), NGs.get(i));
            setAverage(averages, examinationPoint);
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
        switch (clazz) {
            case ControlCurve:
                return probeNumbers.stream().map(pattern_point -> (
                        pattern_point == 1 || pattern_point == 2) ? TOTAL :
                        pattern_point == 3 || pattern_point == 4 || pattern_point == 5 ? NSB :
                                pattern_point == 6 || pattern_point == 7 || pattern_point == 8 ? ZERO
                                        : pattern_point < 23 ? getPattern(pattern, pattern_point) : CONTROL_POINT)
                        .collect(Collectors.toList());
            case ExaminationPoint:
                return probeNumbers.stream().skip(CONTROL_CURVE_POINTS).map(probeNumber -> {
                    int position = probeNumber - CONTROL_CURVE_LENGTH;
                    return isOdd(position) ? (position + 1) / 2 : position / 2;
                }).map(String::valueOf).collect(Collectors.toList());
            default:
                throw new ClassCastException();
        }
    }

    private List<Boolean> isFlagged(List<Integer> CPMs) {
        List<Integer> points = setProbeNumber(CPMs.size());
        List<Boolean> NSB = getZeroOrNsbFlag(CPMs, NSB_START, NSB_END);
        List<Boolean> Zeros = getZeroOrNsbFlag(CPMs, ZERO_START, ZERO_END);
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
//        counter = new Counter();
        counter.createStandardListWithCPMs(controlCurves);
        counter.setStandardsCpmWithFlags(controlCurves);
        counter.bindingPercent();
        counter.logarithmRealZero();
        counter.countRegressionParameterB();
        counter.countRegressionParameterA();
        return CPMs.stream()
                .map(point -> counter.countNg(Double.valueOf(point)))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    private void setStandardPattern(String fileData) {
        if (fileData.equals(CORTISOL_5MIN)) {
            counter.logDose();
        }
    }

    /**
     * meterRead represents level of hormone value in Control Curve Point,
     * ie: expected in position 8 of Cortisol Curve is 1.25 pg per ml,
     * and meterRead could be 0.89 pg per ml
     *
     * @param pattern       read hormone pattern from file
     * @param controlCurves List of already generated Control Curve values
     * @return values of actual read pattern level
     * @throws ControlCurveException is thrown when ControlCurve wasn't created, so generates a meterRead isn't possible
     */
    private List<Double> setMeterRead(String pattern, List<ControlCurve> controlCurves) throws ControlCurveException {
        try {
            counter.createStandardListWithCPMs(controlCurves);
            counter.setStandardsCpmWithFlags(controlCurves);
            counter.bindingPercent();
            counter.logarithmRealZero();
            counter.logDose();
            counter.setCorrelation(getStandardPattern2(pattern));
            counter.setZeroBindingPercent();
            counter.countRegressionParameterB();
            counter.countRegressionParameterA();
            return counter.countMeterReading();
        } catch (Exception error) {
            LOGGER.error(error.getMessage());
        }
        return new ArrayList<>();
    }

    private List<Double> getAverage(List<String> NGs) {
        List<Double> result = new ArrayList<>();
        double pointA = UNKNOWN_POINT, pointB = UNKNOWN_POINT;
        for (String ng : NGs) {
            if (pointA == UNKNOWN_POINT && ng != null) {
                pointA = Double.parseDouble(ng);
            } else if (pointB == UNKNOWN_POINT && ng != null) {
                pointB = Double.parseDouble(ng);
            }
            if (pointA != UNKNOWN_POINT && pointB != UNKNOWN_POINT) {
                double average = (pointA + pointB) / HALF;
                average = avoidNaNsOrInfinite(average);
                result.add(average);
                pointA = UNKNOWN_POINT;
                pointB = UNKNOWN_POINT;
            }
        }
        return result;
    }

    private void setAverage(List<Double> averages, ExaminationPoint examinationPoint) {
        if (isOdd(examinationPoint.getProbeNumber())) {
            int j = Integer.parseInt(examinationPoint.getPosition()) - 1;
            examinationPoint.setAverage(averages.get(j));
        }
    }
}
