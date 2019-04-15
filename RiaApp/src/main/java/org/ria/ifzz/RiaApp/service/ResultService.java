package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.*;
import org.ria.ifzz.RiaApp.repository.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.DomainConstants.FILE_CONTENT;
import static org.ria.ifzz.RiaApp.domain.DomainConstants.RESULT_POINTER;
import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@RestController
public class ResultService {

    private final CustomFileReader customFileReader;
    private final CountResultUtil countResultUtil;
    private final FileUtils fileUtils;
    private final ControlCurveRepository controlCurveRepository;
    private final ControlCurveService controlCurveService;
    private final DataAssigner dataAssigner;

    public ResultService(CustomFileReader customFileReader, CountResultUtil countResultUtil, FileUtils fileUtils, ControlCurveRepository controlCurveRepository, ControlCurveService controlCurveService, DataAssigner dataAssigner) {
        this.customFileReader = customFileReader;
        this.countResultUtil = countResultUtil;
        this.fileUtils = fileUtils;
        this.controlCurveRepository = controlCurveRepository;
        this.controlCurveService = controlCurveService;
        this.dataAssigner = dataAssigner;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * takes file store in local disc space
     *
     * @param data data from uploaded file
     * @return expected List of Strings
     * @throws IOException
     */
    public List<String> getFileData(DataFileMetadata data) throws IOException {
        System.out.println(FILE_CONTENT);
        return customFileReader.readFromStream(data);
    }

    /**
     * takes fileName of upload file and set specific id for each entities
     * reads given Strings List and create Result entity for each lines of given,
     *
     * @param data pre-cleaned list
     * @param file
     * @return Result entities
     */
    public List<Result> setResultFromColumnsLength(List<String> data,
                                                   @NotNull DataFileMetadata file,
                                                   Backlog backlog) {

        Result result;
        List<Result> results = new ArrayList<>();
        for (String dataLine : data) {
            if (dataLine.startsWith(RESULT_POINTER)) {
                result = new Result();
                result.setFileName(data.indexOf(dataLine) + "_" + fileUtils.setFileName(file));
                result.setBacklog(backlog);
                results.add(result);
            }
        }
        return results;
    }

    /**
     * find Result entity in database by {@code}fileName_index,
     * which is created by file's fileName + _ + index, and
     * then assign results from "CCMP" table
     *
     * @param fileData generated by upload file
     */
    public List<Result> assignDataToResult(List<String> fileData,
                                           FileEntity fileEntity,
                                           List<Result> results) {
        String fileId = fileEntity.getDataId();
        List<Result> resultsWithData = new ArrayList<>();
        List<Result> assignedResults = new ArrayList<>();

        resultsWithData = dataAssigner.setCpm(fileData, results);
        for (int i = 0; i < resultsWithData.size(); i++) {
            Result result = resultsWithData.get(i);
            assignedResults.add(result);
        }
        resultsWithData = dataAssigner.setSamples(fileData, fileId, results);
        for (int i = 0; i < resultsWithData.size(); i++) {
            Result result = resultsWithData.get(i);
            assignedResults.add(result);
        }

        resultsWithData = dataAssigner.setPosition(fileData, results);
        for (int i = 0; i < resultsWithData.size(); i++) {
            Result result = resultsWithData.get(i);
            assignedResults.add(result);
        }
        String assignedResultsSize = String.valueOf(assignedResults.size());
        logger.info("ResultService.assignedResults.size(): " + assignedResultsSize);
        return assignedResults;
    }

    private void setStandardPattern(List<String> fileData) {
        if (fileData.get(0).equals("KORTYZOL_5_MIN")) {
            logger.info("Pattern detected: " + fileData.get(0));
            countResultUtil.logDose(CORTISOL_PATTERN);
        }
    }

    private void setBindingPercent(List<Double> curve, List<Point> points, List<ControlCurve> controlCurveList) {
        setControlPointsFromControlCurve(controlCurveList, curve, points);
        countResultUtil.setControlCurveCpmWithFlag(points);
        countResultUtil.setStandardsCpmWithFlags(points);
        countResultUtil.bindingPercent();
    }

    /**
     * @param fileData         contains data which will be assign to Result
     * @param controlCurveList curve points
     * @param results          list of the Result entities with assigned data
     * @return list of Result entities with calculated mass of the hormone in nanograms
     */
    public List<Result> assignNgPerMl(List<String> fileData,
                                      List<ControlCurve> controlCurveList,
                                      List<Result> results) {

        List<Point> points = new ArrayList<>();
        List<Double> curve = new ArrayList<>();
        setStandardPattern(fileData);
        setBindingPercent(curve, points, controlCurveList);
        countResultUtil.logarithmRealZero();
        countResultUtil.countRegressionParameterB();
        countResultUtil.countRegressionParameterA();
        return getCountedResults(fileData, results);
    }

    private List<Result> getCountedResults(List<String> list, List<Result> results) {
        List<Result> countedResults = new ArrayList<>();
        for (int i = 25; i < list.size(); i++) {
            Result result = results.get(i);
            double point = result.getCpm();
            double counted = countResultUtil.countResult(point);
            if (Double.isNaN(counted)) {
                counted = 0.0;
            }
            result.setNg(counted);
            countedResults.add(result);
        }
        return countedResults;
    }

    private void setControlPointsFromControlCurve(List<ControlCurve> controlCurveList,
                                                  List<Double> curve,
                                                  List<Point> points) {
        ControlCurve controlCurve;
        //set cpm values to control curve points
        for (int i = 0; i < 24; i++) {
            controlCurve = controlCurveList.get(i);
            Double pointValue = controlCurve.getCpm();
            Boolean flag = controlCurve.isFlagged();
            curve.add(pointValue);
            Point point = new Point(pointValue, flag);
            points.add(point);
        }
    }

    public List<Result> setDataToResult(@NotNull DataFileMetadata file,
                                        List<String> data,
                                        Backlog backlog,
                                        FileEntity fileEntity) {

        List<Result> resultsWithData = new ArrayList<>();
        List<Result> resultListWithNg = new ArrayList<>();
        List<ControlCurve> controlCurveList;
        List<ControlCurve> controlCurveListWithData;
        List<Result> results = setResultFromColumnsLength(data, file, backlog);

        if (data.size() > 24) {
            try {
                resultsWithData = assignDataToResult(data, fileEntity, results);
            } catch (Exception e) {
                logger.error("Assign Data To Result: " + e.getMessage() + " | " + e.getCause());
            }
        }

        controlCurveList = controlCurveService.setControlCurveFromFileData(data, file, backlog);
        controlCurveListWithData = controlCurveService.setDataToControlCurve(data, fileEntity, controlCurveList);

        //Check if any of standard points are above Zeros points
        if (isStandardCpmAboveZero(controlCurveListWithData)) {
            List<Point> points = new ArrayList<>();
            List<Double> curve = new ArrayList<>();
            setBindingPercent(curve, points, controlCurveList);
            countResultUtil.logarithmRealZero();
            controlCurveRepository.saveAll(controlCurveListWithData);
            return resultsWithData;
        }
        //is they aren't ng will be set to results
        else {
            try {
                resultListWithNg = assignNgPerMl(data, controlCurveListWithData, resultsWithData);
            } catch (Exception e) {
                logger.error("Exception ng: " + e.getMessage() + " with cause: " + e.getCause());
            }
            controlCurveRepository.saveAll(controlCurveListWithData);
//            try{
                dataAssigner.setHormoneAverage(resultListWithNg);
//            } catch (Exception e) {
//                logger.error("Exception average: " + e.getMessage() + " with cause: " + e.getCause());
//            }
            return resultListWithNg;
        }
    }

    /**
     * @param controlCurves list of curve points
     * @return false if any controlCurve points is not flagged (is false),
     * and true if any from those are flagged
     */
    private boolean isStandardCpmAboveZero(List<ControlCurve> controlCurves) {
        boolean checker = false;
        boolean isAbove;
        new ControlCurve();
        ControlCurve controlCurve;
        for (int i = 8; i < 21; i++) {
            controlCurve = controlCurves.get(i);
            if (controlCurve.isFlagged()) {
                checker = true;
                logger.warn("Standard point with CPM value " + controlCurve.getCpm() + " are above zero ");
            }
        }
        isAbove = checker;
        return isAbove;
    }
}

