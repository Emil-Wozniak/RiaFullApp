package org.ria.ifzz.RiaApp.utils;

import lombok.Getter;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountResultUtil {

    private Double total;
    private Double zero;
    private Double nsb;
    private Double binding;

    @Getter
    private List<Double> curve;
    @Getter
    private List<Double> standardsCMP;
    @Getter
    private List<Point> curveFlagged;
    @Getter
    private List<Point> standardsCpmFlagged;
    @Getter
    private List<Double> logDoseList;
    private double[] logDoseListArray;
    private List<Double> bindingPercent;
    @Getter
    private List<Double> logarithmRealZeroTable;
    private double[] logarithmRealZeroArray;
    private Double regressionParameterB;
    private Double regressionParameterA;

    ResultMath resultMath = new ResultMath();

    /**
     * @return List which represents first 8 points of Control Curve (T, N, O)
     * and average results of total, zero and nsb
     */
//    public List<Double> setControlCurveCMP(List<Double> controlCurve) {
//        curve = new ArrayList<>();
//        if (!controlCurve.isEmpty()) {
//            curve.addAll(controlCurve);
//            double t1 = 0;
//            double t2 = 0;
//            double zero1 = 0;
//            double zero2 = 0;
//            double zero3 = 0;
//            double n1 = 0;
//            double n2 = 0;
//            double n3 = 0;
//
//            for (int i = 0; i < 8; i++) {
//                t1 = curve.get(0);
//                t2 = curve.get(1);
//                zero1 = curve.get(2);
//                zero2 = curve.get(3);
//                zero3 = curve.get(4);
//                n1 = curve.get(5);
//                n2 = curve.get(6);
//                n3 = curve.get(7);
//            }
//            // set Total ZERO NSBN
//            total = resultMath.averageTwo(t1, t2);
//            zero = resultMath.averageThree(zero1, zero2, zero3);
//            nsb = resultMath.averageThree(n1, n3, n2);
//
//            // J18 = I18 - I16
//            binding = nsb - zero;
//            System.out.println("Curve:");
//            curve.forEach(System.out::println);
//            System.out.println("T: " + total + " | O: " + nsb + " | N: " + zero + " ==> N - O = " + binding);
//        }
//        return curve;
//    }

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
                nsb1 = curveFlagged.get(5); //should be
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
            System.out.println("CurveFlagged:");
            System.out.println("Flagged T: " + total + " | Zero: " + zero + " | NSB: " + nsb + " ==> N - O = " + binding);
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

    //tableC && tableG -> Control Curve CCMP

    /**
     * //     * @param controlCurve array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     *
     * @return array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     */
//    public List<Double> setStandardsCMP(List<Double> controlCurve) {
//        standardsCMP = new ArrayList<>();
//        if (standardsCMP.size() < 8) {
//            for (int i = 8; i < controlCurve.size() - 2; i++) {
//                double point = controlCurve.get(i);
//                standardsCMP.add(point);
//            }
//        }
//        System.out.println("\nStandard CMP: ");
//        standardsCMP.forEach(System.out::println);
//        return standardsCMP;
//    }

    //Standard storing CMP and Flag
    public List<Double> setStandardsCpmWithFlags(List<Point> points) {
        standardsCpmFlagged = new ArrayList<>();
        standardsCMP = new ArrayList<>();

        if (standardsCpmFlagged.size() < 8) {
            System.out.println("\nStandard CPM_FLAG: ");
            for (int i = 8; i < points.size() - 2; i++) {
                Point point = points.get(i);
                Double pointValue = point.getValue();
                standardsCMP.add(pointValue);
                System.out.println(pointValue + " | " + point.isFlag());
            }
        }
        return standardsCMP;
    }

    //table M == table I

    /**
     * take double array which contains standardized pattern and
     * performs a logarithmic function for each elements of the array
     *
     * @param results array of hormone standardized pattern e.g CORTISOL_PATTERN
     * @return Double List
     */
    public List<Double> logDose(double[] results) {
        List<Double> standardPattern = new ArrayList<>();
        System.out.println("\n\nStandard points:" + "\n======================================================");
        for (double point : results) {
            standardPattern.add(point);
        }
        logDoseList = resultMath.logarithmTable2(standardPattern);
        return logDoseList;
    }

    // Table H == %(N-O)
    // =(G23-$I$16)*100/$J$18

    /**
     * subtracts each value from standards CMP List by each value result from logDose(),
     * then multiply each result by 100, divides those by binding
     *
     * @return
     */
    public List<Double> bindingPercent() {
        System.out.println("\n\nbinding percent" + "\n======================================================");
        List<Double> subtraction = resultMath.subtractTablesElement(standardsCMP, zero);
        List<Double> multiplication = resultMath.multiplyList(100.0, subtraction);
        List<Double> result = resultMath.divideTableCeilElements(binding, multiplication); // %Bo-Bg
        System.out.println("\nBinding percent:");
        result.forEach(System.out::println);
        bindingPercent = result;
        return bindingPercent;
    }

    /*
    Table J
    =LOG(H23/(100-H23))
     */
    public List<Double> logarithmRealZero() {
        System.out.println("\n\nLogarithm Real Zero:" + "\n======================================================");
        List<Double> subtractPercentNO = resultMath.subtractTableElements(100.0, bindingPercent);
        List<Double> divideTable = resultMath.divisionTable(bindingPercent, subtractPercentNO);
        List<Double> logTable = resultMath.logarithmTable2(divideTable);
        System.out.println("\nLogarithm Real Zero: ");
        logTable.forEach(System.out::println);
        logarithmRealZeroTable = logTable;
        return logarithmRealZeroTable;
    }

    /*
    R19 == N20
    var M25:M40 => logDose
    var N25:N40 => logarithmRealZero
    * = sum(N25:N40)/ count(M25:M40)- N19 => regressionParameterB* sum(M25:M40) / count(M25:M40)
    */
    public Double countRegressionParameterA() {
        System.out.println("\n\ncountRegressionParameterA:" +
                "\n======================================================");
        Double sumLogRealZero = resultMath.sum(logarithmRealZeroTable);
        Double countLogDose = resultMath.count(logDoseList);
        System.out.println("regressionParameterB: " + regressionParameterB);
        Double sumLogDose = resultMath.sum(logDoseList);
        regressionParameterA = sumLogRealZero / countLogDose - regressionParameterB * sumLogDose / countLogDose;
        System.out.println("Regression Parameter A = " + regressionParameterA);
        return regressionParameterA;
    }

    /* Excel version:
    N19
    var M25:M40 => logDose
    var N25:N40 => logarithmRealZero
    *
    * =(COUNT(M25:M40) *SUMPRODUCT(M25:M40;N25:N40) -SUM(M25:M40)*SUM(N25:N40))/(COUNT(M25:M40)*SUMSQ(M25:M40)-(SUM(M25:M40))^2)
     */
    public Double countRegressionParameterB() {
        List<Double> LRZ1 = resultMath.logarithmTable1(logarithmRealZeroTable);

        System.out.println("\n\nregressionParameterB:" +
                "\n======================================================");
        Double firstFactor;
        Double secondFactor;
        System.out.println("\nFirst factor");
        Double logDoseCount = resultMath.count(logDoseList);
        Double sum = resultMath.sumProduct(logDoseList, LRZ1); // logarithmRealZeroTable in this place have to be in first flouting point
        Double sumLogDose = resultMath.sum(logDoseList);
        Double sumLogRealZero = resultMath.sum(logarithmRealZeroTable);

        firstFactor = (logDoseCount * sum) - (sumLogDose * sumLogRealZero);
        Double firstFactor2 = Precision.round(firstFactor, 2);
        System.out.println("First " + firstFactor2);

        System.out.println("\nSecond factor");
        Double countSecond = resultMath.count(logDoseList);
        Double sumsqSecondFactor = resultMath.sumsq(logDoseList);
        Double sqr = resultMath.sum(logDoseList);
        sqr = Math.pow(sqr, 2);
        System.out.println("Powered: " + sqr);

        secondFactor = countSecond * sumsqSecondFactor - sqr;
        System.out.println("Second " + secondFactor + "\n");

        Double resultSum = firstFactor / secondFactor;
        Double roundResult = Precision.round(resultSum, 4);
        regressionParameterB = roundResult;
        System.out.println("regressionParameterB result: " + regressionParameterB);
        return regressionParameterB;
    }

    /* Excel version:
     * = 10^ * (( LOG((B44-$I$16) *100 /$J$18 / (100-(B44-$I$16) *100 /$J$18)) -$R$19) /$R$20)
     */

    /**
     * calculates the value of hormone nanograms by formula:
     * { 10^(( LOG((cmp-zero) *100 / binding / (100-(cmp-zero) *100 / binding)) - regressionParameterA) / regressionParameterB)}
     *
     * @param CMP ccmp value from file
     * @return the value of hormone nanograms in the sample
     */
    public Double countResult(Double CMP) {
        System.out.println("\n\nCount Result:" +
                "\n======================================================");
        System.out.println("CMP: " + CMP);
        System.out.println("Zero: " + zero);
        System.out.println("NSB: " + nsb);
        System.out.println("Binding: "+ binding);
        Double firstPart = ((Math.log10((CMP - zero) * 100 / binding / (100 - (CMP - zero) * 100 / binding)) - regressionParameterA) / regressionParameterB);
        Double power = Math.pow(10, firstPart);
        power = Precision.round(power, 2);
        return power;
    }

    public double setCorrelation() {
        Double correlation;
        logDoseListArray = new double[logDoseList.size()];
        for (int i = 0; i < logDoseList.size(); i++) logDoseListArray[i] = logDoseList.get(i);
        logarithmRealZeroArray = new double[logarithmRealZeroTable.size()];
        for (int i = 0; i < logarithmRealZeroTable.size(); i++)
            logarithmRealZeroArray[i] = logarithmRealZeroTable.get(i);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        correlation = pearsonsCorrelation.correlation(logDoseListArray, logarithmRealZeroArray);
        correlation = Precision.round(correlation, 4);
        Double correlationPow = Math.pow(correlation, 2);
        System.out.println("Correlation: " + correlationPow);
        return correlationPow;
    }


}

