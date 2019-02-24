package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.repositorie.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@Service
public class ControlCurveService {

    private final CustomFileReader customFileReader;
    private final ControlCurveRepository controlCurveRepository;
    private final CountResultUtil countResultUtil;
    private final ResultService resultService;

    public ControlCurveService(CustomFileReader customFileReader, ControlCurveRepository controlCurveRepository, CountResultUtil countResultUtil, ResultService resultService) {
        this.customFileReader = customFileReader;
        this.controlCurveRepository = controlCurveRepository;
        this.countResultUtil = countResultUtil;
        this.resultService = resultService;
    }

    public ControlCurve setControlCurveFromColumnsLength(List<String> list, @NotNull MultipartFile file, Backlog backlog) {
        ControlCurve controlCurve = null;

        for (String line : list) {
            if (line.startsWith(" \tUnk")) {
                controlCurve = new ControlCurve();

                //set fileName followed by '_lineIndex'
                controlCurve.setFileName("row_" + list.indexOf(line) + "_" + resultService.setFileName(file));
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

        //Assign position to Result
        for (int i = 0; i < list.size() - 1; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);

            int index = i + 1;
            controlCurve = controlCurveRepository.findByFileName("row_" + index + "_" + resultService.setFileName(file));

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
            } else if (i > 7 && i < 20) {
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
        for (int i = 0; i < list.size() - 1; i++) {
            List Samples = customFileReader.getMatchingStrings(list, 1);

            int index = i + 1;
            controlCurve = controlCurveRepository.findByFileName("row_" + index + "_" + resultService.setFileName(file));
            controlCurve.setDataId(fileId);

            String cleanedSamples = Samples.get(i).toString();
            String replacedSamples = cleanedSamples.replace("Unk_", "");
            Integer samplesInt = Integer.parseInt(replacedSamples);
            controlCurve.setSamples(samplesInt);
            System.out.println(" \tResult samples value: " + controlCurve.getSamples());
        }

        return controlCurve;
    }
}
