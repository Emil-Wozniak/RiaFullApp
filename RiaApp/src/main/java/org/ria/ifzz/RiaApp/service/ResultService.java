package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.*;
import org.ria.ifzz.RiaApp.exception.CurveException;
import org.ria.ifzz.RiaApp.repository.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.ria.ifzz.RiaApp.utils.FileUtils;
import org.ria.ifzz.RiaApp.utils.Point;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@RestController
public class ResultService {

    private final CustomFileReader customFileReader;
    private final CountResultUtil countResultUtil;
    private final FileUtils fileUtils;
    private final ControlCurveRepository controlCurveRepository;
    private final ControlCurveService controlCurveService;

    public ResultService(CustomFileReader customFileReader, CountResultUtil countResultUtil, FileUtils fileUtils, ControlCurveRepository controlCurveRepository, ControlCurveService controlCurveService) {
        this.customFileReader = customFileReader;
        this.countResultUtil = countResultUtil;
        this.fileUtils = fileUtils;
        this.controlCurveRepository = controlCurveRepository;
        this.controlCurveService = controlCurveService;
    }

    /**
     * takes file store in local disc space
     *
     * @param model data from uploaded file
     * @return expected List of Strings
     * @throws IOException
     */
    public List<String> getFileData(FileModel model) throws IOException {
        System.out.println(customFileReader.getUploadComment());
        List<String> streamRead = customFileReader.readFromStream(model);
        return streamRead;
    }

    /**
     * takes fileName of upload file and set specific id for each entities
     * reads given Strings List and create Result entity for each lines of given,
     *
     * @param list pre-cleaned list
     * @param file
     * @return Result entities
     */
    public List<Result> setResultFromColumnsLength(List<String> list, @NotNull FileModel file, Backlog backlog, Result result) {
        List<Result> results = new ArrayList<>();
        for (String line : list) {
            if (line.startsWith(" \tUnk")) {
                result = new Result();
                result.setFileName("row_" + list.indexOf(line) + "_" + fileUtils.setFileName(file));
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
     * @param list generated by upload file
     */
    public List<Result> assignDataToResult(List<String> list, FileEntity fileEntity, List<Result> results) {

        String fileId = fileEntity.getDataId();
        List<Result> resultsWithData = new ArrayList<>();
        //Assign CCMP to Result
        for (int i = 24; i < list.size() - 1; i++) {
            List CCMP = customFileReader.getMatchingStrings(list, 3);
            Result result = results.get(i);
            String ccmpString = CCMP.get(i).toString();
            Double ccmpInteger = Double.parseDouble(ccmpString);
            result.setCcpm(ccmpInteger);
            resultsWithData.add(result);
        }

        //Assign position to Result
        for (int i = 24; i < list.size() - 1; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);
            Result result = results.get(i);
            result.setPosition(position.get(i).toString());
            resultsWithData.add(result);
        }

        //Assign samples to Result
        for (int i = 24; i < list.size() - 1; i++) {
            List Samples = customFileReader.getMatchingStrings(list, 1);
            Result result = results.get(i);
            result.setDataId(fileId);

            String cleanedSamples = Samples.get(i).toString();
            String replacedSamples = cleanedSamples.replace("Unk_", "");
            Integer samplesInt = Integer.parseInt(replacedSamples);
            result.setSamples(samplesInt);
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    /**
     * @param list             contains data which will be assign to Result
     * @param controlCurveList curve points
     * @param results          list of the Result entities with assigned data
     * @return list of Result entities with calculated mass of the hormone in nanograms
     */
    public List<Result> assignNgPerMl(List<String> list, List<ControlCurve> controlCurveList, List<Result> results) {

        Result result = new Result();
        List<Result> countedResults = new ArrayList<>();
        List<Double> curve = new ArrayList<>();
        ControlCurve controlCurve;
        List<Point> points = new ArrayList<>();

        // get standard pattern
        System.out.println("Pattern detected:");
        if (list.get(0).equals("KORTYZOL_5_MIN")){
            System.out.println(list.get(0));
            countResultUtil.logDose(CORTISOL_PATTERN);
        }

        //set ccmp values to control curve points
        try {
            for (int i = 0; i < 24; i++) {
                controlCurve = controlCurveList.get(i);
                Double pointValue = controlCurve.getCcpm();
                Boolean flag = controlCurve.isFlagged();
                curve.add(pointValue);
                Point point = new Point(pointValue, flag);
                points.add(point);
            }
        } catch (Exception exception) {
            throw new CurveException("\nFile " + curve.toString() + " doesn't have a proper size; \nIt must contain at least 24 line for curve and 2 line of results;\n" + exception.getCause());
        }

        countResultUtil.setControlCurveCpmWithFlag(points);
        countResultUtil.setStandardsCpmWithFlags(points);

        countResultUtil.bindingPercent();
        countResultUtil.logarithmRealZero();
        countResultUtil.countRegressionParameterB();
        countResultUtil.countRegressionParameterA();

        List<Double> countedList = new ArrayList<>();
        for (int i = 25; i < list.size(); i++) {
            result = results.get(i);
            double point = result.getCcpm();
            double counted = countResultUtil.countResult(point);
            countedList.add(counted);
            if (Double.isNaN(counted)) {
                counted = 0.0;
            }
            result.setNg(counted);
            countedResults.add(result);
        }
        return countedResults;
    }

    //TODO catch NonResultException
    public List<Result> setDataToResult(@NotNull FileModel file, List<String> list, Backlog backlog, FileEntity fileEntity) {
        Result newResult = new Result();
        List<Result> resultsWithData = new ArrayList<>();
        List<Result> resultListWithNg = new ArrayList<>();

        List<Result> results = setResultFromColumnsLength(list, file, backlog, newResult);
        try {
            resultsWithData = assignDataToResult(list, fileEntity, results);
        } catch (Exception e) {
            System.out.println("Assign Data To Result: "  + e.getMessage() + " | " + e.getCause());
        }
        List<ControlCurve> controlCurveList = controlCurveService.setControlCurveFromColumnsLength(list, file, backlog);
        List<ControlCurve> controlCurveListWithData = controlCurveService.setDataToControlCurve(list, fileEntity, controlCurveList);
        controlCurveRepository.saveAll(controlCurveListWithData);
        try {
            resultListWithNg = assignNgPerMl(list, controlCurveListWithData, resultsWithData);
        } catch (Exception e) {
            System.out.println("Exception ng: " + e.getMessage() + " | " + e.getCause());
        }

        return resultListWithNg;
    }
}

