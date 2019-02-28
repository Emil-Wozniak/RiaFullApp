package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repository.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.ria.ifzz.RiaApp.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@Service
public class ControlCurveService {

    private final CustomFileReader customFileReader;
    private final ControlCurveRepository controlCurveRepository;
    private final FileUtils fileUtils;
    private final FileEntityService fileEntityService;

    public ControlCurveService(CustomFileReader customFileReader, ControlCurveRepository controlCurveRepository, ResultService resultService, FileUtils fileUtils, FileEntityService fileEntityService) {
        this.customFileReader = customFileReader;
        this.controlCurveRepository = controlCurveRepository;
        this.fileUtils = fileUtils;
        this.fileEntityService = fileEntityService;
    }

    public ControlCurve setControlCurveFromColumnsLength(List<String> list, @NotNull MultipartFile file, Backlog backlog) {
        ControlCurve controlCurve = null;

        for (int i = 0; i <25; i++){
            String line = list.get(i);
            if (line.startsWith(" \tUnk")) {
                controlCurve = new ControlCurve();

                //set fileName followed by '_lineIndex'
                controlCurve.setFileName("row_" + i+ "_" + fileUtils.setFileName(file));
                controlCurve.setBacklog(backlog);
                controlCurveRepository.save(controlCurve);
                System.out.println("Control Curve " + controlCurve.getFileName() + " saved");
            }
        }
        return controlCurve;
    }

    public ControlCurve setDataToControlCurve(List<String> list, @NotNull MultipartFile file, FileEntity fileEntity) {

        ControlCurve controlCurve = null;
        String fileId = fileEntity.getDataId();

        //Assign CCMP to Result
        for (int i = 0; i < 24; i++) {
            List CCMP = customFileReader.getMatchingStrings(list, 3);

            int index = i + 1;
            controlCurve = controlCurveRepository.findByFileName("row_" + index + "_" + fileUtils.setFileName(file));

            // convert String value of CCMP to Integer
            String ccmpString = CCMP.get(i).toString();
            Double ccmpInteger = Double.parseDouble(ccmpString);
            controlCurve.setCcpm(ccmpInteger);
            System.out.println(" \tControl Curve CCMP value: " + controlCurve.getCcpm());
        }

        //Assign position to Result
        for (int i = 0; i < 24; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);

            int index = i + 1;
            controlCurve = controlCurveRepository.findByFileName("row_" + index + "_" + fileUtils.setFileName(file));

            if (i == 0 || i == 1) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("A", "T");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);

            } else if (i == 2 || i == 3 || i == 4) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("A", "O");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
            } else if (i == 5 || i == 6 || i == 7) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("[A-Z]", "N");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
            } else if (i < 20) {
                String preConvertedPosition = position.get(i).toString();
                double point = CORTISOL_PATTERN[i - 7];
                String convert = preConvertedPosition.replaceAll("[0-9]", "");
                String postConvert = convert.replaceAll("[A-Z]", String.valueOf(point));
                controlCurve.setPosition(postConvert);
            } else if (i == 22 || i == 23) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("[A-Z]", "K");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
            }
        }

        //Assign samples to Result
        for (int i = 0; i < 25 - 1; i++) {

            int index = i + 1;
            controlCurve = controlCurveRepository.findByFileName("row_" + index + "_" + fileUtils.setFileName(file));
            controlCurve.setDataId(fileId);
            controlCurve.setSamples(i);
            System.out.println(" \tResult samples value: " + controlCurve.getSamples());
        }

        return controlCurve;
    }

    public Iterable<ControlCurve> findCCBacklogByDataId(String dataId) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);
        return controlCurveRepository.findByDataIdOrderByFileName(dataId);
    }

    public ControlCurve findResultByDataId(String dataId, String fileName) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);

        ControlCurve controlCurve = controlCurveRepository.findByFileName(fileName);
        if (controlCurve == null) {
            throw new FileEntityNotFoundException("File with ID: '" + fileName + "' not found");
        }
        if (!controlCurve.getDataId().equals(dataId)) {
            throw new FileEntityNotFoundException("Curve '" + fileName + "' does not exist: '" + dataId);
        }
        return controlCurve;
    }
}
