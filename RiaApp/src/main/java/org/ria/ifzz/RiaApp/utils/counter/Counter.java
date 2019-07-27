package org.ria.ifzz.RiaApp.utils.counter;

import lombok.Getter;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Precision;
import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static org.ria.ifzz.RiaApp.models.pattern.HormonesPattern.CORTISOL_PATTERN;
import static org.ria.ifzz.RiaApp.services.examination.FileExtractor.avoidNaNsOrInfinite;
import static org.ria.ifzz.RiaApp.utils.counter.Algorithms.*;

@Component
public class Counter implements Algorithms {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Integer total;
    private Integer zero;
    private Integer nsb;
    private Integer binding;

    @Getter
    private List<Double> logDoseList;
    @Getter
    private List<Double> logarithmRealZeroTable;
    @Getter
    private Double regressionParameterB;
    private Double regressionParameterA;
    private List<Double> bindingPercent;
    private List<Integer> standardsCPM;

    public void createStandardListWithCPMs(List<ControlCurve> controlCurves) throws ControlCurveException {
        try {
            List<ControlCurve> NSBs = getPoint(controlCurves, 5, 7);
            List<ControlCurve> ZEROs = getPoint(controlCurves, 2, 4);
            total = (controlCurves.get(0).getCpm() + controlCurves.get(1).getCpm()) / 2;
            nsb = calculateByFlagsValue(NSBs);
            zero = calculateByFlagsValue(ZEROs);
            binding = nsb - zero;
            LOGGER.info("T: " + total + " | Zero: " + zero + " | NSB: " + nsb + " | N - O: " + binding);
        } catch (Exception curveException) {
            throw new ControlCurveException("Control curve too small, size: " + controlCurves.size());
        }
    }

    /**
     * takes Point parameters and checks if one of them is flagged,
     * if does perform correct algorithm which ignore flagged element
     *
     * @return average value between two or three of Points value which are not flagged
     */
    private Integer calculateByFlagsValue(List<ControlCurve> controlCurve) {
        Integer output = null;
        if ((controlCurve.get(0).isFlagged()) || (controlCurve.get(1).isFlagged()) || (controlCurve.get(2).isFlagged())) {
            if (!controlCurve.get(0).isFlagged() && !controlCurve.get(1).isFlagged()) {
                output = (controlCurve.get(0).getCpm() + controlCurve.get(1).getCpm()) / 2;
            } else if (!controlCurve.get(1).isFlagged() && !controlCurve.get(2).isFlagged()) {
                output = (controlCurve.get(1).getCpm() + controlCurve.get(2).getCpm()) / 2;
            } else if (!controlCurve.get(2).isFlagged() && !controlCurve.get(0).isFlagged()) {
                output = (controlCurve.get(2).getCpm() + controlCurve.get(0).getCpm()) / 2;
            }
        } else {
            output = (controlCurve.get(0).getCpm() + controlCurve.get(1).getCpm() + controlCurve.get(2).getCpm()) / 3;
        }
        return output;
    }

    private List<ControlCurve> getPoint(List<ControlCurve> controlCurves, int from, int to) {
        List<ControlCurve> selectedPoints = new ArrayList<>();
        for (int i = from; i < to + 1; i++) {
            ControlCurve controlCurve = controlCurves.get(i);
            selectedPoints.add(controlCurve);
        }
        return selectedPoints;
    }

    //Standard storing CMP and Flag
    //tableC && tableG -> Control Curve CCMP

    /**
     * * @param controlCurve array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     */
    public void setStandardsCpmWithFlags(List<ControlCurve> controlCurve) {
        standardsCPM = new ArrayList<>();
        for (int i = 8; i < 22; i++) {
            standardsCPM.add(controlCurve.get(i).getCpm());
        }
    }

    /**
     * take double array which contains standardized pattern and
     * performs a logarithmic function for each elements of the array
     */
    public void logDose() {
        List<Double> standardPattern = new ArrayList<>();
        for (double point : CORTISOL_PATTERN) {
            standardPattern.add(point);
        }
        logDoseList = logarithmTable2(standardPattern);
    }

    // Table H == %(N-O)
    // =(G23-$I$16)*100/$J$18

    /**
     * subtracts each value from standards CMP List by each value result from logDose(),
     * then multiply each result by 100, divides those by binding
     */
    public void bindingPercent() {
        List<Double> subtraction = subtractTablesElement(standardsCPM, zero);
        List<Double> multiplication = multiplyList(100.0, subtraction);
        List<Double> result = divideTableCeilElements(binding, multiplication);
        if (result != null) {
            for (Double pointer : result) {
                if (pointer.isNaN() || pointer.isInfinite()) {
                    LOGGER.warn("Binding percent has infinite or NaN points;");
                }
            }
            LOGGER.info("Binding percent: " + Arrays.toString(result.toArray()));
        }
        bindingPercent = result;
    }

    /*
    Table J
    =LOG(H23/(100-H23))
     */
    public void logarithmRealZero() {
        List<Double> logTable = null;
        try {
            List<Double> subtractPercentNO = subtractTableElements(100.0, bindingPercent);
            List<Double> divideTable = divisionTable(bindingPercent, subtractPercentNO);
            logTable = logarithmTable2(divideTable);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
        if (logTable != null) {
            for (Double point : logTable) {
                if (point.isNaN() || point.isInfinite()) {
                    LOGGER.warn("Logarithm Real Zero has infinite or NaN points;");
                }
            }
            LOGGER.info("Logarithm Real Zero: " + ": " + Arrays.toString(logTable.toArray()));
        }
        logarithmRealZeroTable = logTable;
    }

    /*
    R19 == N20
    var M25:M40 => logDose
    var N25:N40 => logarithmRealZero
    * = sum(N25:N40)/ count(M25:M40)- N19 => regressionParameterB* sum(M25:M40) / count(M25:M40)
    */
    public void countRegressionParameterA() {
        try {
            Double sumLogRealZero = sum(logarithmRealZeroTable);
            Double countLogDose = count(logDoseList);
            Double sumLogDose = sum(logDoseList);
            regressionParameterA = sumLogRealZero / countLogDose - regressionParameterB * sumLogDose / countLogDose;
            LOGGER.info("Regression Parameter A = " + regressionParameterA);
        } catch (Exception error) {
            LOGGER.error(error.getMessage());
        }
    }

    /* Excel version:
    N19
    var M25:M40 => logDose
    var N25:N40 => logarithmRealZero
    *
    * =(COUNT(M25:M40) *SUMPRODUCT(M25:M40;N25:N40) -SUM(M25:M40)*SUM(N25:N40))/(COUNT(M25:M40)*SUMSQ(M25:M40)-(SUM(M25:M40))^2)
     */
    public void countRegressionParameterB() {
        try {
            List<Double> realZeroPrecision1 = logarithmTable1(logarithmRealZeroTable);
            double firstFactor;
            double secondFactor;
            Double logDoseCount = count(logDoseList);
            Double sum = sumProduct(logDoseList, realZeroPrecision1); // logarithmRealZeroTable in this place have to be in first flouting point
            Double sumLogDose = sum(logDoseList);
            Double sumLogRealZero = sum(logarithmRealZeroTable);
            firstFactor = getPointSubtract(logDoseCount * sum, sumLogDose * sumLogRealZero);
            double firstFactorInPrecision2 = Precision.round(firstFactor, 2);

            Double countSecond = count(logDoseList);
            Double sumsqSecondFactor = sumsq(logDoseList);
            double sqr = sum(logDoseList);
            sqr = pow(sqr, 2);

            secondFactor = getPointSubtract(countSecond * sumsqSecondFactor, sqr);

            double resultSum = firstFactor / secondFactor;
            regressionParameterB = Precision.round(resultSum, 4);
        } catch (Exception error) {
            LOGGER.error(error.getMessage() + "has occurred");
        }
    }

    /* Excel version:
     * = 10^ * (( LOG((B44-$I$16) *100 /$J$18 / (100-(B44-$I$16) *100 /$J$18)) -$R$19) /$R$20)
     */

    /**
     * calculates the value of hormone nanograms by formula:
     * { 10^(( LOG((cmp-zero) *100 / binding / (100-(cmp-zero) *100 / binding)) - regressionParameterA) / regressionParameterB)}
     *
     * @param CPM ccmp value from file
     * @return the value of hormone nanograms in the sample
     */
    public Double countNg(Double CPM) {
        double result = ((log10((CPM - zero.doubleValue()) * 100 / binding.doubleValue() / (100 - (CPM - zero.doubleValue()) * 100 / binding.doubleValue())) - regressionParameterA) / regressionParameterB);
        double power = pow(10, result);
        power = Precision.round(power, 2);
        return avoidNaNsOrInfinite(power);
    }

    public double setCorrelation(List<Double> pattern) {
        double[] logDoseListArray = new double[pattern.size()];
        double[] logarithmRealZeroArray = new double[logarithmRealZeroTable.size()];
        for (int i = 0; i < logDoseList.size(); i++) {
            logDoseListArray[i] = logDoseList.get(i);
        }
        for (int i = 0; i < logarithmRealZeroTable.size(); i++) {
            logarithmRealZeroArray[i] = logarithmRealZeroTable.get(i);
        }
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(logDoseListArray, logarithmRealZeroArray);
        double correlationPow = pow(correlation, 2);
        correlationPow = Precision.round(correlationPow, 6);
        LOGGER.info("Correlation: " + correlationPow);
        return correlationPow;
    }

    public double setZeroBindingPercent() {
        double zeroBindingPercent = binding * 100 / total;
        zeroBindingPercent = Precision.round(zeroBindingPercent, 2);
        LOGGER.info("Zero binding percent: " + zeroBindingPercent);
        return zeroBindingPercent;
    }

    public List<Double> countMeterReading() {
        return standardsCPM.stream().map(point -> Precision.round((pow(10, ((log10(getPointSubtract(point, zero)
                * 100 / binding / (100 - (getPointSubtract(point, zero))
                * 100 / binding)) - regressionParameterA) / regressionParameterB))), 4))
                .collect(Collectors.toList());
    }
}

