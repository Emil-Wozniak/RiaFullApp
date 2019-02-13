package org.ria.ifzz.RiaApp.utils;

import lombok.Data;
import org.apache.poi.ss.formula.functions.Sumproduct;

import java.util.*;

@Data
public class CountResult {

    private Integer t1;   // T
    private Integer t2;
    private Integer o1;   // Bg
    private Integer o2;   // Bg
    private Integer n1;   // Bo
    private Integer n2;   // Bo
    private Integer n3;   // Bo

    private Sumproduct sumproduct = new Sumproduct();

    private double total = averageTwo(t1,t2);
    private double zero = averageTwo(o1,o2);
    private double nonSpecificBinding = averageThree(n1,n2, n3);
    private double binding = nonSpecificBinding - zero;

    private double[] standardCortisol = {1.25, 2.5, 5, 10, 20, 40, 80};

    private double[] standardCCMP; //tableC && tableG
    private double[] tableH = subtractTable(binding, multiply(100, subtractTable(zero, standardCCMP))); //TODO make this executable
    private double[] tableJ = logarithmTables(tableH,100,tableH); //elements table H

    private double stnd1 = doseLog(standardCortisol[1]);
    private double [] tableO;
    private double[] tableN = tableJ;
    private double[] tableM;
    private double n18 = count(tableM); // regression parameter N (amount)
    private double n19 = count(tableM) * sumProduct(tableM,tableN)+ sum(tableN)-sum(tableM)*sum(tableN)/(count(tableM)* sumSQ(tableM)-Math.pow(sum(tableM),2));
    private double n20 =sum(tableN)/count(tableM)-n19*sum(tableM)/count(tableM); // regression parameter a
    private double r19 = sum(tableN)/ count(tableM)-n19*sum(tableM)/count(tableM);
    private double r20 = n19;  // regression parameter b

    public double countResult(double ccmp){
        double product = Math.pow(((Math.log((ccmp- zero)*100/binding/(100-(ccmp-zero)*100/binding))-r19)/r20),10);
        return product;
    }

    public Integer averageTwo(Integer first, Integer second) {
        return (first + second) / 2;
    }

    public Integer averageThree(Integer first, Integer second, Integer third) {
        return (first + second + third) / 3;
    }

    public double doseLog(double standard){
        return Math.log(standard);
    }

    public static double[] subtractTable(double subtrahend, double... values) {
        List<Double> result = new ArrayList<>();
        for (double value:values){
            result.add(value - subtrahend);
        }
        double[] convertedDoubleList = result.stream().mapToDouble(d -> d).toArray();
        return convertedDoubleList;
    }

    public static double[] logarithmTables(double[] divisionTable, double subtrahend, double[]subtractTable){

        double[] logarithmicNumbers = {};
        for (int i = 0; i < divisionTable.length; i++) {
            logarithmicNumbers = divisionTable(divisionTable, subtractTable(subtrahend,subtractTable[i]));
        }
        for (double logarithmBase : logarithmicNumbers){
            Math.log(logarithmBase);
        }
        return logarithmicNumbers;
    }

    public static double[] divisionTable(double[] factor, double[]multiplier){

        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < factor.length; i++) {

            product = factor[i] / multiplier[i];
            productTable.add(product);
        }
        double[] convertedDoubleList = productTable.stream().mapToDouble(d -> d).toArray();
        return convertedDoubleList;
    }

    public static double multiply(double multiplier, double...values) {
        double result = 0;
        for (double value:values)
            result = value * multiplier;
        return result;
    }

    public static double sum(double...values) {
        double result = 0;
        for (double value:values)
            result += value;
        return result;
    }

    public static double count(double[] values){
        return values.length;
    }

    public static double sumProduct(double[] factor, double[]multiplier){

        double product;
        List<Double> productTable = new ArrayList<>();
        double result = 0;
        for (int i = 0; i < factor.length; i++) {

            product = factor[i] * multiplier[i];
            productTable.add(product);
        }
        for (double value : productTable) {
            result += value;
        }
        return result;
    }

    public static double sumSQ(double[] values){
        double result = 0;
        for (double value:values){
            result = Math.pow(value, 2);
        }
        return result;
    }
}
