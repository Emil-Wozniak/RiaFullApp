package org.ria.ifzz.RiaApp.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.domain.HormonesPattern.CORTISOL_PATTERN;

@Service
public class CountResultUtil {

    private Double total;
    private Double zero;
    private Double nonSpecificBinding;
    private Double binding;
    private List<Double> curve;

    ResultMath resultMath = new ResultMath();

    public List<Double> setControlCurveCCMP(List<Double> controlCurve) {
        curve = new ArrayList<>();
        if (!controlCurve.isEmpty()) {
            for (double point : controlCurve) {
                curve.add(point);
                System.out.println(point);
            }
            double t1 = 0;
            double t2 = 0;
            double zero1 = 0;
            double zero2 = 0;
            double zero3 = 0;
            double n1 = 0;
            double n2 = 0;
            double n3 = 0;

            // set Total
            for (int i = 0; i < curve.size(); i++) {
                t1 = curve.get(0);
                t2 = curve.get(1);
                zero1 = curve.get(2);
                zero2 = curve.get(3);
                zero3 = curve.get(4);
                n1 = curve.get(5);
                n2 = curve.get(7);
                n3 = curve.get(6);
            }

            total = resultMath.AVERAGE_TWO(t1, t2);
            zero = resultMath.AVERAGE_THREE(zero1, zero2, zero3);
            nonSpecificBinding = resultMath.AVERAGE_THREE(n1, n3, n2);

            binding = nonSpecificBinding - zero;
            System.out.println("Curve:");
            curve.forEach(System.out::println);
            System.out.println("T: " + total + " | N: " + nonSpecificBinding + " | O: " + zero + " >>> N - O = " + binding);
        }
        return curve;
    }


    //tableC && tableG -> Control Curve CCMP
    public List<Double> addStandardCCP() {
        List<Double> result = new ArrayList<>();
        if (result.isEmpty()) {
            result.add(402.0);
            result.add(392.0);
            result.add(358.0);
            result.add(349.0);
            result.add(289.0);
            result.add(299.0);
            result.add(208.0);
            result.add(200.0);
            result.add(144.0);
            result.add(155.0);
            result.add(9.0);
            result.add(104.0);
            result.add(67.0);
            result.add(73.0);
        }
        System.out.println("Standard CCMP:");
        result.forEach(System.out::println);
        return result;
    }

    //table M == table I
    public List<Double> doseLog(double[] result) {
        List<Double> standardPattern = new ArrayList<>();
        System.out.println("Standard points:");
        if (standardPattern.isEmpty()) {
            for (double point : result) {
                standardPattern.add(point);
                System.out.println(point);
            }
        }
        List<Double> dose = resultMath.logarithmTable(standardPattern);
        System.out.println("doseLog curve size: " + dose.size());
        return dose;
    }

    /*
    Table H %Bo-Bg
    Bo = N
    Bg = O
     */
    public List<Double> percentN_Per_O() {
        List<Double> subtraction = resultMath.subtractTablesElement(addStandardCCP(), doseLog(CORTISOL_PATTERN));
        List<Double> multiplication = resultMath.multiplyList(100.0, subtraction);
        List<Double> result = resultMath.divideTableElements(binding, multiplication); // %Bo-Bg
        return result;
    }

    /*
    Table J
    =(G23-$I$16)*100/$J$18
     */
    public List<Double> logitRealZero() {
        List<Double> tableJ;
        List<Double> subtractPercentNO = resultMath.subtractTable(100.0, percentN_Per_O());
        System.out.println("subtractPercentNO size" + subtractPercentNO.size() + "\n\n");
        List<Double> divideTable = resultMath.divisionTable(percentN_Per_O(), subtractPercentNO);
        System.out.println("\n\ndivideTable:");
        divideTable.forEach(System.out::println);
//        tableJ = resultMath.LOGARITHM_TABLES(percentN_Per_O(), 100,subtractPercentNO);

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
//        System.out.println(" + (Log()dose + logitrealZero)" + sum_productMJinN);
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

