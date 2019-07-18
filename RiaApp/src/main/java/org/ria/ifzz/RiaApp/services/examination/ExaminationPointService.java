package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.repositories.results.ExaminationPointRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getMatchingString;
import static org.ria.ifzz.RiaApp.utils.EvenOdd.isOdd;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CONTROL_CURVE_LENGTH;
import static org.ria.ifzz.RiaApp.utils.constants.ExaminationConstants.CORTISOL_5MIN;

@Service
public class ExaminationPointService implements CustomFileReader {

    private final CountResultUtil countResultUtil;
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final ExaminationPointRepository examinationPointRepository;

    public ExaminationPointService(CountResultUtil countResultUtil, ExaminationPointRepository examinationPointRepository) {
        this.countResultUtil = countResultUtil;
        this.examinationPointRepository = examinationPointRepository;
    }


    /**
     * checks {@code standardPattern} fromResultPointService metadata and skips
     *
     * @param metadata     data from uploaded file
     * @param controlCurve Control Curve entities
     * @return ExaminationPoint entities
     */
    public List<ExaminationPoint> create(List<String> metadata, List<ControlCurve> controlCurve) {
        String filename = metadata.get(0);
        String pattern = metadata.get(1);
        setStandardPattern(metadata.get(1));
        metadata = metadata.stream().skip(2).collect(Collectors.toList());
        return createExaminationPoint(filename, pattern, metadata, controlCurve);
    }

    /**
     * takes a proper data from metadata,
     * and assigns them to created Result Point properties
     *
     * @param filename     uploaded file title
     * @param pattern      examination pattern from metadata
     * @param metadata     data from uploaded file
     * @param controlCurve Control Curve entities
     * @return ExaminationPoint List
     */
    private List<ExaminationPoint> createExaminationPoint(String filename, String pattern, List<String> metadata, List<ControlCurve> controlCurve) {
        int FILENAME_POSITION = metadata.size() - 1;
        metadata.remove(FILENAME_POSITION);
        List<ExaminationPoint> examinationPoints = new ArrayList<>();
        List<Integer> probeNumbers = setProbeNumber(metadata.size());
        List<String> positions = setPosition(probeNumbers);
        List<Integer> CPMs = setCPMs(metadata);
        List<Boolean> flags = isFlagged(controlCurve, CPMs);
        List<String> NGs = setNg(controlCurve, CPMs);
        for (int i = 0; i < metadata.size() - CONTROL_CURVE_LENGTH; i++) {
            ExaminationPoint examinationPoint = new ExaminationPoint(filename, pattern, probeNumbers.get(i), positions.get(i), CPMs.get(i), flags.get(i), NGs.get(i));
            examinationPoints.add(examinationPoint);
        }
        examinationPointRepository.saveAll(examinationPoints);
        LOGGER.info("Examination points created: " + examinationPoints.size());
        return examinationPoints;
    }

    /**
     * generates List of integers
     *
     * @param to metadata size
     * @return List of integers
     */
    private List<Integer> setProbeNumber(int to) {
        int startPoint = CONTROL_CURVE_LENGTH + 1;
        return IntStream.rangeClosed(startPoint, to).boxed().collect(Collectors.toList());
    }

    /**
     * generates pairs of the same integers
     *
     * @param probeNumbers generated List of integers
     * @return List of integers pair
     */
    private List<String> setPosition(List<Integer> probeNumbers) {
        List<String> positions = new ArrayList<>();
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
    }

    /**
     * collects data from metadata and parse them to integers
     *
     * @param metadata data from uploaded file
     * @return integers from CPM column
     */
    private List<Integer> setCPMs(List<String> metadata) {

        return metadata.stream().skip(CONTROL_CURVE_LENGTH)
                .map(line -> {
                    line = getMatchingString(line, 3);
                    return line;
                })
                .map(Integer::parseInt).collect(Collectors.toList());
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

    List<Boolean> isSpread(List<Integer> CPMs) {
        return isSpread(CPMs);
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

    private void setStandardPattern(String fileData) {
        if (fileData.equals(CORTISOL_5MIN)) {
            LOGGER.info("Pattern detected: " + fileData);
            countResultUtil.logDose();
        }
    }
}
