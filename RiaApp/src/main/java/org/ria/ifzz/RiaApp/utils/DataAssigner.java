package org.ria.ifzz.RiaApp.utils;

import org.ria.ifzz.RiaApp.domain.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataAssigner {

    private final CustomFileReader customFileReader;

    public DataAssigner(CustomFileReader customFileReader) {
        this.customFileReader = customFileReader;
    }

    public List<Result> setCpm(List<String> list, String fileId, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();

        //Assign CCMP to Result
        for (int i = 24; i < list.size() - 1; i++) {
            List CPM = customFileReader.getMatchingStrings(list, 3);
            Result result = results.get(i);
            String cpmString = CPM.get(i).toString();
            Double cpmInteger = Double.parseDouble(cpmString);
            result.setCcpm(cpmInteger);
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    public List<Result> setPosition(List<String> list, String fileId, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();
        //Assign position to Result
        for (int i = 24; i < list.size() - 1; i++) {
            List position = customFileReader.getMatchingStrings(list, 2);
            Result result = results.get(i);
            result.setPosition(position.get(i).toString());
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    public List<Result> setSamples(List<String> list, String fileId, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();
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

}
