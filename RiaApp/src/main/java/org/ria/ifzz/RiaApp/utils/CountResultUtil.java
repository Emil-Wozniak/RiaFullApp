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
            total = resultMath.AVERAGE_TWO(t1, t2);
            zero = resultMath.AVERAGE_THREE(zero1, zero2, zero3);
            nonSpecificBinding = resultMath.AVERAGE_THREE(n1, n3, n2);

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
     *
     * @param controlCurve array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     * @return array of CMP of hormone standardized pattern e.g CORTISOL_PATTERN
     */
    public List<Double> setStandardsCMP(List<Double> controlCurve) {
        standardsCMP = new ArrayList<>();
        if (standardsCMP.size() < 8) {
            for (int i = 8; i < controlCurve.size()-2; i++) {
                double point = controlCurve.get(i);
                standardsCMP.add(point);
            }
        }
        System.out.println("\nStandard CMP: Size => "+ standardsCMP.size());
        standardsCMP.forEach(System.out::println);
        return standardsCMP;
    }

    //table M == table I
    /**
     *
     * @param result array of hormone standardized pattern e.g CORTISOL_PATTERN
     * @return Double List
     */
    public List<Double> doseLog(double[] result) {
        List<Double> standardPattern = new ArrayList<>();
        System.out.println("Standard points:");
        for (double point : result) {
            standardPattern.add(point);
        }
        logDoseList = resultMath.logarithmTable(standardPattern);
        System.out.println("doseLog curve size: " + logDoseList.size());
        return logDoseList;
    }

    /*
    Table H %Bo-Bg
    Bo = N
    Bg = O
     */
    public List<Double> bindingPercent() {
        List<Double> subtraction = resultMath.subtractTablesElement(standardsCMP, logDoseList);
        List<Double> multiplication = resultMath.multiplyList(100.0, subtraction);
        List<Double> result = resultMath.divideTableElements(binding, multiplication); // %Bo-Bg
        System.out.println("Binding percent:");
        result.forEach(System.out::println);
        return result;
    }

    /*
    Table J
    =(G23-$I$16)*100/$J$18
     */
    public List<Double> logitRealZero() {
        List<Double> subtractPercentNO = resultMath.subtractTable(100.0, bindingPercent());
        System.out.println("subtractPercentNO size" + subtractPercentNO.size() + "\n\n");
        List<Double> divideTable = resultMath.divisionTable(bindingPercent(), subtractPercentNO);
        System.out.println("\n\ndivideTable:");
        divideTable.forEach(System.out::println);
//        tableJ = resultMath.LOGARITHM_TABLES(bindingPercent(), 100,subtractPercentNO);

        System.out.println("End logit RealZero\n\n");
//        tableJ.forEach(System.out::println);
        return divideTable;
    }


//    private Double r20 = countRegressionParameterB();  // regression parameter b
//
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

    /*

    N19
    =(COUNT(M25:M40)*SUMPRODUCT(M25:M40;N25:N40)-SUM(M25:M40)*SUM(N25:N40))/(COUNT(M25:M40)*SUMSQ(M25:M40)-(SUM(M25:M40))^2)
     */
//    public Double countRegressionParameterB() {
//        System.out.println("\n\ncountRegressionParameterB:\n======================================================");
//        Double countRegressionParameterB;
//        Double countMinN = resultMath.COUNT(doseLog());
//        countRegressionParameterB = countMinN;
//        System.out.println("doseLog:\n");
//
//        doseLog().forEach(System.out::println);
//        Double sum_productMJinN = resultMath.SUM_PRODUCT(doseLog(), logitRealZero());
//
//        countRegressionParameterB *= sum_productMJinN;
//        System.out.println(" + (Log()logDoseList + logitrealZero)" + sum_productMJinN);
//
//        Double sumJinN = resultMath.SUM(logitRealZero());
//        countRegressionParameterB += sumJinN;
//        System.out.println(" + SUM(logitRealZero()");
//
//        Double sumMinN = resultMath.SUM(doseLog());
//        countRegressionParameterB -= sumMinN;
//        System.out.println(" + Sum(doseLog()):");
//
//        countRegressionParameterB *= sumJinN;
//        System.out.println(" + SUM(logitRealZero() 2");
//
//        Double sumsqInN = resultMath.SUMSQ(tableM);
//        System.out.println(" + SUMSQ(doseLog)");
//
//        Double powerInN = Math.pow(resultMath.SUM(tableM), 2);
//        System.out.println(" + power(* , 2)");
//        countRegressionParameterB = countRegressionParameterB / (countMinN * sumsqInN - powerInN);
//        System.out.println(countRegressionParameterB);
//        return countRegressionParameterB;
//    }

    /*
        R19 == N20
        =SUM(N25:N40)/COUNT(M25:M40)-N19*SUM(M25:M40)/COUNT(M25:M40)
        */
//    public Double countRegressionParameterA() {
//        System.out.println("\n\ncountRegressionParameterA:\n======================================================");
//        Double regressionParameterA;
//        regressionParameterA = resultMath.SUM(logitRealZero());
//        System.out.println("====>\t\t\t|1");
//        regressionParameterA = regressionParameterA / resultMath.COUNT(tableM);
//        System.out.println("=========>\t\t|2");
//        regressionParameterA = regressionParameterA - countRegressionParameterB();
//        System.out.println("=============>\t|3");
//        regressionParameterA = regressionParameterA * resultMath.SUM(tableM);
//        System.out.println("==================>|4");
//        System.out.println("-> rpa: " + regressionParameterA);
//        System.out.println("-> cTM: " + resultMath.COUNT(tableM));
//        regressionParameterA = regressionParameterA / resultMath.COUNT(tableM);
//
//
//        System.out.println(regressionParameterA);
//        return regressionParameterA;
//    }


}

