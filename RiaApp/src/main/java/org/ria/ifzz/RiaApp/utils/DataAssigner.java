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

    /**
     *
     * @param fileData data from uploaded file
     * @param results List of
     * @return
     */
    public List<Result> setCpm(List<String> fileData, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();

        //Assign CPM to Result
        for (int i = 24; i < fileData.size() - 1; i++) {
            List CPM = customFileReader.getMatchingStrings(fileData, 3);
            Result result = results.get(i);
            String cpmString = CPM.get(i).toString();
            Double cpmInteger = Double.parseDouble(cpmString);
            result.setCpm(cpmInteger);
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    public List<Result> setPosition(List<String> fileData, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();
        //Assign position to Result
        for (int i = 24; i < fileData.size() - 1; i++) {
            List position = customFileReader.getMatchingStrings(fileData, 2);
            Result result = results.get(i);
            result.setPosition(position.get(i).toString());
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    public List<Result> setSamples(List<String> fileData, String fileId, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();
        //Assign samples to Result
        for (int i = 24; i < fileData.size() - 1; i++) {
            List Samples = customFileReader.getMatchingStrings(fileData, 1);
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
