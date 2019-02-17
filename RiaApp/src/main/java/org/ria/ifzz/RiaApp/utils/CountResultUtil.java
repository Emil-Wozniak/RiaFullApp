package org.ria.ifzz.RiaApp.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountResultUtil {

    private Double total;
    private Double zero;
    private Double nonSpecificBinding;
    private Double binding;
    private List<Double> curve;
    private List<Double> standardsCMP;
    private List<Double> logDoseList;
    private List<Double> bindingPercent;
    private List<Double> logitRealZeroTable;
    private Double countRegressionParameterB;

    ResultMath resultMath = new ResultMath();

    /**
     * @param controlCurve CCMP results List
     * @return List which represents first 8 points of Control Curve (T, N, O)
     * and average results of total, zero and nonSpecificBinding
     */
    public List<Double> setControlCurveCMP(List<Double> controlCurve) {
        curve = new ArrayList<>();
        if (!controlCurve.isEmpty()) {
            curve.addAll(controlCurve);
            double t1 = 0;
            double t2 = 0;
            double zero1 = 0;
            double zero2 = 0;
            double zero3 = 0;
            double n1 = 0;
            double n2 = 0;
            double n3 = 0;

            for (int i = 0; i < 8; i++) {
                t1 = curve.get(0);
                t2 = curve.get(1);
                zero1 = curve.get(2);
                zero2 = curve.get(3);
                zero3 = curve.get(4);
                n1 = curve.get(5);
                n2 = curve.get(6);
                n3 = curve.get(7);
            }

            // set Total ZERO NSBN
            total = resultMath.averageTwo(t1, t2);
            zero = resultMath.averageThree(zero1, zero2, zero3);
            nonSpecificBinding = resultMath.averageThree(n1, n3, n2);

            // J18 = I18 - I16
            binding = nonSpecificBinding - zero;
            System.out.println("Curve:");
            curve.forEach(System.out::println);
            System.out.println("T: " + total + " | N: " + nonSpecificBinding + " | O: " + zero + " >>> N - O = " + binding);
        }
        return curve;
    }


    //tableC && tableG -> Control Curve CCMP

    /**
     * @param controlCurve array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     * @return array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     */
    public List<Double> setStandardsCMP(List<Double> controlCurve) {
        standardsCMP = new ArrayList<>();
        if (standardsCMP.size() < 8) {
            for (int i = 8; i < controlCurve.size() - 2; i++) {
                double point = controlCurve.get(i);
                standardsCMP.add(point);
            }
        }
        System.out.println("\nStandard CMP: ");
        standardsCMP.forEach(System.out::println);
        return standardsCMP;
    }

    //table M == table I

    /**
     * take double array which contains standardized pattern and
     * performs a logarithmic function for each elements of the array
     *
     * @param result array of hormone standardized pattern e.g CORTISOL_PATTERN
     * @return Double List
     */
    public List<Double> doseLog(double[] result) {
        List<Double> standardPattern = new ArrayList<>();
        System.out.println("\n\nStandard points:" + "\n======================================================");
        for (double point : result) {
            standardPattern.add(point);
        }
        logDoseList = resultMath.logarithmTable(standardPattern);
        return logDoseList;
    }

    // Table H == %(N-O)
   // =(G23-$I$16)*100/$J$18

    /**
     * subtracts each value from standards CMP List by each value result from doseLog(),
     * then multiply each result by 100, divides those by binding
     *
     * @return
     */
    public List<Double> bindingPercent() {
        System.out.println("\n\nbinding percent" + "\n======================================================");
        List<Double> subtraction = resultMath.subtractTablesElement(standardsCMP, zero);
        List<Double> multiplication = resultMath.multiplyList(100.0, subtraction);
        List<Double> result = resultMath.divideTableElements(binding, multiplication); // %Bo-Bg
        System.out.println("\nBinding percent:");
        result.forEach(System.out::println);
        bindingPercent = result;
        return bindingPercent;
    }

    /*
    Table J
    =LOG(H23/(100-H23))
     */
    public List<Double> logitRealZero() {
        System.out.println("\n\nLogit Real Zero:" + "\n======================================================");
        List<Double> subtractPercentNO = resultMath.subtractTableElements(100.0, bindingPercent);
        List<Double> divideTable = resultMath.divisionTable(bindingPercent, subtractPercentNO);
        logitRealZeroTable = resultMath.logarithmTable(divideTable);
        return logitRealZeroTable;
    }

    /*
    R19 == N20
    var M25:M40 => logDose
    var N25:N40 => logitRealZero
    * = sum(N25:N40)
    * / count(M25:M40)
    * - N19 => countRegressionParameterB
    * * sum(M25:M40)
    * / count(M25:M40)
    */
    public Double countRegressionParameterA() {
        System.out.println("\n\ncountRegressionParameterA:" +
                "\n======================================================");
        Double regressionParameterA;
        regressionParameterA = resultMath.sum(logitRealZeroTable);
        regressionParameterA /= resultMath.count(logDoseList);
        regressionParameterA -= countRegressionParameterB;
        regressionParameterA *= resultMath.sum(logDoseList);
        regressionParameterA /= resultMath.count(logDoseList);

        System.out.println(regressionParameterA);
        return regressionParameterA;
    }

    /*
    N19
    var M25:M40 => logDose
    var N25:N40 => logitRealZero
    = (
    * count(M25:M40)                    logDose
    * *SUMPRODUCT(M25:M40;N25:N40)      logDose logitRealZero
    * -sum(M25:M40)
    * *sum(N25:N40)
    * )
    * /(
    * count(M25:M40)
    * *sumsq(M25:M40)
    * -(sum(M25:M40)
    * )
    * ^2)
     */
    public Double countRegressionParameterB() {
        System.out.println("\n\ncountRegressionParameterB:" +
                "\n======================================================");
        Double firstFactor;
        Double secondFactor;
        System.out.println("\nFirst factor");
        firstFactor = resultMath.count(logDoseList);
        firstFactor *= resultMath.sumProduct(logDoseList, logitRealZeroTable);
        firstFactor -= resultMath.sum(logDoseList);
        System.out.println("["+firstFactor + " * ");
        firstFactor *= resultMath.sum(logitRealZeroTable);
        firstFactor *= -1;
        System.out.println("First " + firstFactor);

        System.out.println("\nSecond factor");
        secondFactor = resultMath.count(logDoseList);
        secondFactor *= resultMath.sumsq(logDoseList);
        Double sqr = resultMath.sum(logDoseList);
        sqr = sqr *sqr;
        secondFactor -=sqr;
        System.out.println("Second "+secondFactor + "\n");

        System.out.println("Math.pow: " + secondFactor);

        countRegressionParameterB = firstFactor / secondFactor;
        System.out.println("countRegressionParameterB result: " + countRegressionParameterB);
        return countRegressionParameterB;
    }

//    public Double countResult(Double ccmp) {
//        System.out.println("======================================================\nStep 1");
//        Double log = Math.log((ccmp - zero) * 100 / binding / (100 - (ccmp - zero) * 100 / binding));
//        System.out.println("======================================================\nStep 2\n" + log);
//        Double logMinusR19 = (log - countRegressionParameterA());
//        System.out.println("======================================================\nStep 3\n" + logMinusR19);
//        Double product = (logMinusR19 / r20);
//        System.out.println("======================================================\nStep 4\n" + product);
//        Math.pow(product, 10);
//        System.out.println("======================================================\nProduct return: \n" + product);
//        return product;
//    }
//
//    public double averageCountedResult(double countResult1, double countResult2) {
//        return (countResult1 + countResult2) / 2;
//    }
}

