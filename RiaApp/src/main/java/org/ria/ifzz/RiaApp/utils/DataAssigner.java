package org.ria.ifzz.RiaApp.utils;

import org.apache.commons.math3.util.Precision;
import org.ria.ifzz.RiaApp.models.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.ria.ifzz.RiaApp.models.DomainConstants.FILEDATA_RESULT_LINE_POINTER;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getMatchingStrings;
import static org.ria.ifzz.RiaApp.utils.EvenOdd.isOdd;

@Service
public class DataAssigner implements CustomFileReader {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public List<Result> setCpm(List<String> data, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();

        //Assign CPM to Result
        for (int i = FILEDATA_RESULT_LINE_POINTER; i < data.size() - 1; i++) {
            List CPM = getMatchingStrings(data, 3);
            Result result = results.get(i);
            String cpmString = CPM.get(i).toString();
            Double cpmInteger = Double.parseDouble(cpmString);
            result.setCpm(cpmInteger);
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    public List<Result> setSamples(List<String> data, String fileId, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();
        for (int i = FILEDATA_RESULT_LINE_POINTER; i < data.size() - 1; i++) {
            List Samples = getMatchingStrings(data, 1);
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

    public List<Result> setPosition(List<String> data, List<Result> results) {
        List<Result> resultsWithData = new ArrayList<>();
        for (int i = FILEDATA_RESULT_LINE_POINTER; i < data.size() - 1; i++) {
            Result result = results.get(i);
            String stringOfPosition = result.getSamples().toString();
            int intPosition = Integer.parseInt(stringOfPosition) - FILEDATA_RESULT_LINE_POINTER;
            if (isOdd(intPosition)) {
                intPosition = (intPosition + 1) / 2;
                stringOfPosition = String.valueOf(intPosition);
            } else {
                intPosition = intPosition / 2;
                stringOfPosition = String.valueOf(intPosition);
            }
            result.setPosition(stringOfPosition);
            resultsWithData.add(result);
        }
        return resultsWithData;
    }

    public List<Result> setHormoneAverage(List<Result> results) {
        List<Result> resultSorted;
        List<Result> averageResults = new ArrayList<>();
        Result[] currentCalculatedResults = new Result[2];
        double ngAverage;
        double positionA = 0.0;
        double positionB;

        resultSorted = results.stream().sorted(Comparator.comparing(Result::getSamples)).collect(Collectors.toList());
        for (int currentResult = 0; currentResult < results.size() - 1; currentResult++) {
            Result result = resultSorted.get(currentResult);
            if (currentCalculatedResults[0] == null) {
                currentCalculatedResults[0] = result;
                positionA = currentCalculatedResults[0].getNg();
            } else {
                currentCalculatedResults[1] = result;
                positionB = currentCalculatedResults[1].getNg();
                ngAverage = (positionA + positionB) / 2;
                ngAverage = Precision.round(ngAverage, 4);
                if (!isNgAverageCorrect(ngAverage, positionA, positionB)) {
                    LOGGER.warn("(A: " + positionA + " + B: " + positionB + ") / 2 = " + ngAverage);
                }
                currentCalculatedResults[0].setHormoneAverage(ngAverage);
                currentCalculatedResults[1].setHormoneAverage(ngAverage);
                averageResults.add(currentCalculatedResults[0]);
                averageResults.add(currentCalculatedResults[1]);
                currentCalculatedResults[0] = null;
                currentCalculatedResults[1] = null;
            }
        }
        return averageResults;
    }

    private boolean isNgAverageCorrect(double ngAverage, double positionA, double positionB) {
        double checking = (positionA + positionB);
        return checking > ngAverage / 10;
    }
}
