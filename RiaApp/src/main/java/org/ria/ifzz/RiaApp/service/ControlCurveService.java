package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repository.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.ria.ifzz.RiaApp.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@Service
public class ControlCurveService {

    private final CustomFileReader customFileReader;
    private final ControlCurveRepository controlCurveRepository;
    private final FileUtils fileUtils;
    private final FileEntityService fileEntityService;

    public ControlCurveService(CustomFileReader customFileReader, ControlCurveRepository controlCurveRepository, FileUtils fileUtils, FileEntityService fileEntityService) {
        this.customFileReader = customFileReader;
        this.controlCurveRepository = controlCurveRepository;
        this.fileUtils = fileUtils;
        this.fileEntityService = fileEntityService;
    }

    public List<ControlCurve> setControlCurveFromColumnsLength(List<String> list, @NotNull MultipartFile file, Backlog backlog) {
        List<ControlCurve> controlCurveList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            String line = list.get(i);
            if (line.startsWith(" \tUnk")) {
                ControlCurve controlCurvePoint = new ControlCurve();
                controlCurvePoint.setFileName("row_" + i + "_" + fileUtils.setFileName(file));
                controlCurvePoint.setBacklog(backlog);
                System.out.println("Control Curve " + controlCurvePoint.getFileName() + " saved");
                controlCurveList.add(controlCurvePoint);
            }
        }
        return controlCurveList;
    }

    public List<ControlCurve> setDataToControlCurve(List<String> list, FileEntity fileEntity, List<ControlCurve> curveList) {

        List<ControlCurve> controlCurveList = new ArrayList<>();
        String fileId = fileEntity.getDataId();
        int index = 0;

        ControlCurve controlCurve;
        //Assign CCMP to Result
        for (int i = 0; i < 24; i++) {
            List CCMP = customFileReader.getMatchingStrings(list, 3);

            index = i;
            controlCurve = curveList.get(index);

            // convert String value of CCMP to Integer
            String ccmpString = CCMP.get(i).toString();
            Double ccmpInteger = Double.parseDouble(ccmpString);
            controlCurve.setCcpm(ccmpInteger);
            System.out.println(" \tControl Curve CCMP value: " + controlCurve.getCcpm());
            controlCurveList.add(controlCurve);
        }

        //Assign position to Result
        for (int i = 0; i < 24; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);

            index = i;
            controlCurve = curveList.get(index);

            if (i == 0 || i == 1) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("A", "Total");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
                controlCurveList.add(controlCurve);

            } else if (i == 2 || i == 3 || i == 4) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("A", "NSB");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
                controlCurveList.add(controlCurve);

            } else if (i == 5 || i == 6 || i == 7) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("[A-Z]", "O");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
                controlCurveList.add(controlCurve);

            } else if (i < 22) {
                String preConvertedPosition = position.get(i).toString();
                double point = CORTISOL_PATTERN[i - 8];
                System.out.println("POINT: " + point);
                String convert = preConvertedPosition.replaceAll("[0-9]", "");
                String postConvert = convert.replaceAll("[A-Z]", String.valueOf(point));
                controlCurve.setPosition(postConvert);
                controlCurveList.add(controlCurve);
            } else if (i == 22 || i == 23) {
                String preConvertedPosition = position.get(i).toString();
                String converted = preConvertedPosition.replaceAll("[A-Z]", "K");
                String postConvert = converted.replaceAll("[0-9]", "");
                controlCurve.setPosition(postConvert);
                controlCurveList.add(controlCurve);
            }
        }

        //Assign samples to Result
        for (int i = 0; i < 25 - 1; i++) {
            index = i;
            controlCurve = curveList.get(index);
            controlCurve.setDataId(fileId);
            controlCurve.setSamples(i);
            System.out.println(" \tResult samples value: " + controlCurve.getSamples());
            controlCurveList.add(controlCurve);
        }
        return controlCurveList;
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
