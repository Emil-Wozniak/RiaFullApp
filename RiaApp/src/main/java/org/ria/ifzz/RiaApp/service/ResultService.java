package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.exception.CurveException;
import org.ria.ifzz.RiaApp.repositorie.ResultRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@RestController
public class ResultService {

    @Autowired
    FileEntityService fileEntityService;

    private final CustomFileReader customFileReader;
    private final ResultRepository resultRepository;
    private final CountResultUtil countResultUtil;

    public ResultService(CustomFileReader customFileReader, ResultRepository resultRepository, CountResultUtil countResultUtil) {
        this.customFileReader = customFileReader;
        this.resultRepository = resultRepository;
        this.countResultUtil = countResultUtil;
    }

    /**
     * takes file store in local disc space
     *
     * @param file uploaded file
     * @return expected List of Strings
     * @throws IOException
     */
    public List<String> getFileData(MultipartFile file) throws IOException {
        System.out.println(customFileReader.getUploadComment());

        List<String> fileLineList = customFileReader.readStoredTxtFile(file);
        List<String> expectedLineList = customFileReader.removeUnnecessaryLineFromListedFile(fileLineList);
        return expectedLineList;
    }

    /**
     * takes fileName of upload file and set specific id for each entities
     * reads given Strings List and create Result entity for each lines of given,
     *
     * @param list pre-cleaned list
     * @param file
     * @return Result entities
     */
    public Result setResultFromColumnsLength(List<String> list, @NotNull MultipartFile file, Backlog backlog) {
        Result result = null;

        for (String line : list) {
            if (line.startsWith(" \tUnk")) {
                result = new Result();

                //set fileName followed by '_lineIndex'
                result.setFileName("row_" + list.indexOf(line) + "_" + setFileName(file));
                result.setBacklog(backlog);

                resultRepository.save(result);
            }
        }
        return result;
    }

    /**
     * find Result entity in database by {@code}fileName_index,
     * which is created by file's fileName + _ + index, and
     * then assign results from "CCMP" table
     *
     * @param list generated by upload file
     * @param file
     */
    public Result assignDataToResult(List<String> list, @NotNull MultipartFile file, FileEntity fileEntity) {

        Result result = null;
        String fileId = fileEntity.getDataId();

        //Assign CCMP to Result
        for (int i = 0; i < list.size() - 1; i++) {
            List CCMP = customFileReader.getMatchingStrings(list, 3);

            int index = i + 1;
            result = resultRepository.findByFileName("row_" + index+ "_" + setFileName(file));

            // convert String value of CCMP to Integer
            String ccmpString = CCMP.get(i).toString();
            Double ccmpInteger = Double.parseDouble(ccmpString);
            result.setCcpm(ccmpInteger);
            System.out.println(" \tResult CCMP value: " + result.getCcpm());
        }

        //Assign position to Result
        for (int i = 0; i < list.size() - 1; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);

            int index = i + 1;
            result = resultRepository.findByFileName("row_" + index+ "_" + setFileName(file));

            result.setPosition(position.get(i).toString());
            System.out.println(" \tResult position value: " + result.getPosition());
        }

        //Assign samples to Result
        for (int i = 0; i < list.size() - 1; i++) {
            List Samples = customFileReader.getMatchingStrings(list, 1);

            int index = i + 1;
            result = resultRepository.findByFileName("row_" + index+ "_" + setFileName(file));
            result.setDataId(fileId);
            result.setSamples(Samples.get(i).toString());
            System.out.println(" \tResult samples value: " + result.getSamples());
        }

        return result;
    }

    public Result assignNgPerMl(@NotNull MultipartFile file) {

        Result result = new Result();
        List<Double> curve = new ArrayList<>();

        // get standard pattern
        countResultUtil.doseLog(CORTISOL_PATTERN);

        // get all results of control curve (Totals, ZEROs, NSBNs) + control points (t1 + t2)
        try {
            for (int i = 1; i < 25; i++) {
                result = resultRepository.findByFileName("row_" + i + "_" + setFileName(file));
                double point = result.getCcpm();
                curve.add(point);
            }
        } catch (Exception exception) {
            throw new CurveException("\nFile " + file.getOriginalFilename() + " doesn't have a proper size; \nIt must contain at least 24 line for curve and 2 line of results;\n" + exception.getCause());
        }

        countResultUtil.setControlCurveCMP(curve);
        countResultUtil.setStandardsCMP(curve);
        countResultUtil.bindingPercent();
        countResultUtil.logitRealZero();

        countResultUtil.countRegressionParameterB();
        countResultUtil.countRegressionParameterA();
        return result;
    }


    public String setFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        fileName.replace(".txt", "").toString();
        return fileName;
    }
}

