package org.ria.ifzz.RiaApp.utils;

import lombok.Getter;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CountResultUtil {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Double total;
    private Double zero;
    private Double nsb;
    private Double binding;

    @Getter
    private List<Double> standardsCPM;
    @Getter
    private List<Point> curveFlagged;
    @Getter
    private List<Point> standardsCpmFlagged;
    @Getter
    private List<Double> logDoseList = new ArrayList<>();
    private List<Double> bindingPercent = new ArrayList<>();
    @Getter
    private List<Double> logarithmRealZeroTable = new ArrayList<>();
    private Double regressionParameterB;
    private Double regressionParameterA;

    private ResultMath resultMath = new ResultMath();

    /**
     * @param points list containing CMPs and flags
     * @return List which represents first 8 points of Control Curve (T, N, O)
     * and average results of total, zero and nsb
     */
    public List<Point> setControlCurveCpmWithFlag(List<Point> points) {
        curveFlagged = new ArrayList<>();
        if (!points.isEmpty()) {
            curveFlagged.addAll(points);
            Point t1 = new Point();
            Point t2 = new Point();
            Point nsb1 = new Point();
            Point nsb2 = new Point();
            Point nsb3 = new Point();
            Point zero1 = new Point();
            Point zero2 = new Point();
            Point zero3 = new Point();

            for (int i = 0; i < 8; i++) {
                t1 = curveFlagged.get(0);
                t2 = curveFlagged.get(1);
                nsb1 = curveFlagged.get(5);
                nsb2 = curveFlagged.get(6);
                nsb3 = curveFlagged.get(7);
                zero1 = curveFlagged.get(2);
                zero2 = curveFlagged.get(3);
                zero3 = curveFlagged.get(4);
            }

            total = resultMath.averageTwo(t1.getValue(), t2.getValue());
            nsb = isControlCurveHasFlag(nsb1, nsb2, nsb3);
            zero = isControlCurveHasFlag(zero1, zero2, zero3);

            binding = nsb - zero;
            logger.info("T: " + total + " | Zero: " + zero + " | NSB: " + nsb + " | N - O: " + binding);
        }
        return curveFlagged;
    }

    /**
     * takes Point parameters and checks if one of them is flagged,
     * if does perform correct algorithm which ignore flagged element
     *
     * @param first  Point
     * @param second Point
     * @param third  Point
     * @return average value between two or three of Points value which are not flagged
     */
    private double isControlCurveHasFlag(Point first, Point second, Point third) {
        Double output = null;
        if (first.isFlag() || second.isFlag() || third.isFlag()) {
            if (!first.isFlag() && !second.isFlag()) {
                output = resultMath.averageTwo(first.getValue(), second.getValue());
            } else if (!second.isFlag() && !third.isFlag()) {
                output = resultMath.averageTwo(second.getValue(), third.getValue());
            } else if (!third.isFlag() && !first.isFlag()) {
                output = resultMath.averageTwo(third.getValue(), first.getValue());
            }
        } else {
            //handle if every of points are false
            output = resultMath.averageThree(first.getValue(), second.getValue(), third.getValue());
        }
        return output;
    }

    //Standard storing CMP and Flag
    //tableC && tableG -> Control Curve CCMP

    /**
     * * @param controlCurve array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     *
     * @return array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     */
    public void setStandardsCpmWithFlags(List<Point> points) {
        standardsCpmFlagged = new ArrayList<>();
        standardsCPM = new ArrayList<>();

        if (standardsCpmFlagged.size() < 8) {
            for (int i = 8; i < points.size() - 2; i++) {
                Point point = points.get(i);
                Double pointValue = point.getValue();
                standardsCPM.add(pointValue);
            }
        }
    }

    //table M == table I

    /**
     * take double array which contains standardized pattern and
     * performs a logarithmic function for each elements of the array
     *
     * @param results array of hormone standardized pattern e.g CORTISOL_PATTERN
     * @return Double List
     */
    public void logDose(double[] results) {
        List<Double> standardPattern = new ArrayList<>();
        for (double point : results) {
            standardPattern.add(point);
        }
        logDoseList = resultMath.logarithmTable2(standardPattern);
        logger.info("Standard points:" + Arrays.toString(results));
    }

    // Table H == %(N-O)
    // =(G23-$I$16)*100/$J$18

    /**
     * subtracts each value from standards CMP List by each value result from logDose(),
     * then multiply each result by 100, divides those by binding
     *
     * @return
     */
    public void bindingPercent() {
        List<Double> subtraction = resultMath.subtractTablesElement(standardsCPM, zero);
        List<Double> multiplication = resultMath.multiplyList(100.0, subtraction);
        List<Double> result = resultMath.divideTableCeilElements(binding, multiplication); // %Bo-Bg
        if (result!=null) {
            for (int i = 0; i < result.size(); i++) {
                Double pointer;
                pointer = result.get(i);
                if (pointer.isNaN() || pointer.isInfinite()) {
                    logger.warn("Binding percent has infinite or NaN points;");
                } else {
                    logger.info("Binding percent: " + i + ": " + pointer);
                }
            }
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
            List<Double> subtractPercentNO = resultMath.subtractTableElements(100.0, bindingPercent);
            List<Double> divideTable = resultMath.divisionTable(bindingPercent, subtractPercentNO);
            logTable = resultMath.logarithmTable2(divideTable);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
        if (logTable!=null) {
            for (int i = 0; i < logTable.size(); i++) {
                if (logTable.get(i).isNaN() || logTable.get(i).isInfinite()) {
                    logger.warn("Logarithm Real Zero has infinite or NaN points;");
                } else {
                    logger.info("Logarithm Real Zero: " + i + ": " + logTable.get(i));
                }
            }
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
        Double sumLogRealZero = resultMath.sum(logarithmRealZeroTable);
        Double countLogDose = resultMath.count(logDoseList);
        Double sumLogDose = resultMath.sum(logDoseList);
        regressionParameterA = sumLogRealZero / countLogDose - regressionParameterB * sumLogDose / countLogDose;
        logger.info("Regression Parameter A = " + regressionParameterA);
    }

    /* Excel version:
    N19
    var M25:M40 => logDose
    var N25:N40 => logarithmRealZero
    *
    * =(COUNT(M25:M40) *SUMPRODUCT(M25:M40;N25:N40) -SUM(M25:M40)*SUM(N25:N40))/(COUNT(M25:M40)*SUMSQ(M25:M40)-(SUM(M25:M40))^2)
     */
    public void countRegressionParameterB() {
        List<Double> realZeroPrecision1 = resultMath.logarithmTable1(logarithmRealZeroTable);
        double firstFactor;
        double secondFactor;
        Double logDoseCount = resultMath.count(logDoseList);
        Double sum = resultMath.sumProduct(logDoseList, realZeroPrecision1); // logarithmRealZeroTable in this place have to be in first flouting point
        Double sumLogDose = resultMath.sum(logDoseList);
        Double sumLogRealZero = resultMath.sum(logarithmRealZeroTable);
        firstFactor = (logDoseCount * sum) - (sumLogDose * sumLogRealZero);
        double firstFactorInPrecision2 = Precision.round(firstFactor, 2);

        Double countSecond = resultMath.count(logDoseList);
        Double sumsqSecondFactor = resultMath.sumsq(logDoseList);
        double sqr = resultMath.sum(logDoseList);
        sqr = Math.pow(sqr, 2);

        secondFactor = countSecond * sumsqSecondFactor - sqr;

        Double resultSum = firstFactor / secondFactor;
        Double roundResult = Precision.round(resultSum, 4);
        regressionParameterB = roundResult;
        logger.info("Regression Parameter B = " + regressionParameterB);
        logger.info("First factor " + firstFactorInPrecision2 + "\t[count: " + logDoseCount + "\t|sumproduct:  " + sum + "\t\t|sumLogDose: " + sumLogDose + "\t\t|sumLogRealZero: " + sumLogRealZero + "\t]");
        logger.info("Second factor: " + secondFactor + "\t[count logDose: " + countSecond + "\t|sumsq logDose: " + sumsqSecondFactor + "\t|square logDose: " + sqr + "\t]");
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
    public Double countResult(Double CPM) {
        Double firstPart = ((Math.log10((CPM - zero) * 100 / binding / (100 - (CPM - zero) * 100 / binding)) - regressionParameterA) / regressionParameterB);
        double power = Math.pow(10, firstPart);
        power = Precision.round(power, 2);
        if (!Double.isNaN(power)) {
            logger.info("Count Result: [CPM: " + CPM + "\t|\t" + power + " ng\t]");
        } else {
            logger.warn("Count Result: [CPM: " + CPM + "\t|\t" + power + " ng\t]");
        }
        return power;
    }

    public double setCorrelation() {
        double[] logDoseListArray = new double[logDoseList.size()];
        for (int i = 0; i < logDoseList.size(); i++) logDoseListArray[i] = logDoseList.get(i);
        double[] logarithmRealZeroArray = new double[logarithmRealZeroTable.size()];
        for (int i = 0; i < logarithmRealZeroTable.size(); i++)
            logarithmRealZeroArray[i] = logarithmRealZeroTable.get(i);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(logDoseListArray, logarithmRealZeroArray);
        double correlationPow = Math.pow(correlation, 2);
        correlationPow = Precision.round(correlationPow, 6);
        logger.info("Correlation: " + correlationPow);
        return correlationPow;
    }

    public double setZeroBindingPercent() {
        double zeroBindingPercent = binding * 100 / total;
        zeroBindingPercent = Precision.round(zeroBindingPercent, 2);
        logger.info("Zero binding percent: " + zeroBindingPercent);
        return zeroBindingPercent;
    }
}

