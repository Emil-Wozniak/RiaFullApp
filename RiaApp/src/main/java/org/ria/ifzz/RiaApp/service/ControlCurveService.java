package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.FileModel;
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

    public List<ControlCurve> setControlCurveFromColumnsLength(List<String> list, @NotNull FileModel file, Backlog backlog) {
        List<ControlCurve> controlCurveList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String line = list.get(i);
            if (line.startsWith(" \tUnk")) {
                ControlCurve controlCurvePoint = new ControlCurve();
                controlCurvePoint.setFileName("row_" + i + "_" + fileUtils.setFileName(file));
                controlCurvePoint.setBacklog(backlog);
                controlCurveList.add(controlCurvePoint);
            }
        }
        return controlCurveList;
    }

    /**
     * assigns data from list to ControlCurve object
     *
     * @param list contains data
     * @return list of all control curve points
     */
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

        //Check if NSBs or Zeros have too large spread and flag those which are
        if (!controlCurveList.isEmpty()) {
            isSpreadTooLarge(2, 3, 4, controlCurveList, 10);
            isSpreadTooLarge(5, 6, 7, controlCurveList, 10);
        }

        //Assign position to Result
        for (int i = 0; i < 24; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);

            controlCurve = curveList.get(i);

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
            controlCurveList.add(controlCurve);
        }
        return controlCurveList;
    }

    /**
     * takes 3 of NSBs or Zeros curve points and performs setFlag method on them
     *
     * @param first            curve point
     * @param second           curve point
     * @param third            curve point
     * @param controlCurveList list of flagged curve point
     * @param percentage       not accepted percentage difference between the points
     */
    private void isSpreadTooLarge(int first, int second, int third, List<ControlCurve> controlCurveList, int percentage) {
        ControlCurve controlCurve1 = controlCurveList.get(first);
        ControlCurve controlCurve2 = controlCurveList.get(second);
        ControlCurve controlCurve3 = controlCurveList.get(third);
        setFlag(controlCurve1, controlCurve2, controlCurve3, percentage);
    }

    //TODO handle inverse situations (when a < b)
    /**
     * takes absolute values of 3 of NSBs or Zeros curve points and checks if one of them is greater than acceptable percent value,
     * if it is flagged with value "true"
     *
     * @param first   curve point
     * @param second  curve point
     * @param third   curve point
     * @param percent not accepted percentage difference between the points
     */
    public void setFlag(ControlCurve first, ControlCurve second, ControlCurve third, int percent) {
        if (first.getCcpm() != second.getCcpm()) {
            moreOrLess(first, second, percent);
        } else if (second.getCcpm() != third.getCcpm()) {
            moreOrLess(second, third, percent);
        } else if (third.getCcpm() != first.getCcpm()) {
            moreOrLess(third,first,percent);
        }
    }

    private void moreOrLess(ControlCurve factor1, ControlCurve factor2, int percent) {
        if (Math.abs(factor1.getCcpm() - factor2.getCcpm()) == 0){
            System.out.println(factor1.getCcpm() + " and " + factor2.getCcpm() + " are equals " );
        }
        else if (Math.abs(factor1.getCcpm() - factor2.getCcpm()) > (factor1.getCcpm() / percent)){
            System.out.println(factor1.getCcpm() + " is greater than " + percent + "% of " + factor2.getCcpm());
            factor1.setFlagged(true);
        } else if (Math.abs(factor1.getCcpm() - factor2.getCcpm()) < (factor1.getCcpm() / percent)){
            System.out.println(factor1.getCcpm() + " is less than " + percent + "% of " + factor2.getCcpm());
            factor2.setFlagged(true);
        }
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
