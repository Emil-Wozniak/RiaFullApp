package org.ria.ifzz.RiaApp.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultMath {

    public Double AVERAGE_TWO(Double first, Double second) {
        return (first+ second) / 2;
    }

    public  double AVERAGE_THREE(double first, double second, double third) {
       double result = (first+second+third) / 3 ;
        System.out.println("AVERAGE_THREE: " + result);
        return result;
    }

    public  Double SUBTRACT_VALUE(Double subtrahend, Double subtractValue) {
        return subtractValue - subtrahend;
    }

    public  List<Double> subtractTable(Double subtrahend, List<Double> values) {
        List<Double> result = new ArrayList<>();
        for (Double value : values) {
            result.add(value - subtrahend);
        }
        System.out.println("subtractTable: " +result.size());
        result.forEach(System.out::println);
        return result;
    }

    public  List<Double> subtractTablesElement(List<Double> subtrahend, List<Double> values) {
//        System.out.println("subtractTablesElement:");
        Double sub;
        Double value;
        Double result;
//        System.out.println("S:" + subtrahend.size() + " | V: " + values.size());
        List<Double> results = new ArrayList<>();
        for (int i =0; i<subtrahend.size(); i++) {
            sub = subtrahend.get(i);
            value = values.get(i);
            result = sub - value;
            results.add(result);
        }
//        results.forEach(System.out::println);
        return results;
    }

    public  List<Double> divideTableElements(double factor, List<Double> multiplier) {

        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < multiplier.size(); i++) {

            product = multiplier.get(i) / factor;
            productTable.add(product);
//            System.out.println("\n"+i+")");
//            productTable.forEach(System.out::println);
        }
//        System.out.println("\nDIVISION_TABLE:");
        return productTable;
    }

    public  List<Double> logarithmTable(List<Double> logarithmTable) {
        List<Double> newTable = logarithmTable.stream().map(tableElement -> Math.log(tableElement)).collect(Collectors.toList());
        System.out.println("\nLogarithmTable:");
        newTable.forEach(System.out::println);
        return newTable;
    }

    public  List<Double> LOGARITHM_TABLES(List<Double> divisionTable, Double subtrahend, List<Double> subtractTable) {

        List<Double> productTable = new ArrayList<>();
        List<Double> subtractNumbers = new ArrayList<>();
        List<Double> logarithmicNumbers = new ArrayList<>();

//        System.out.println("\nSIZE subtractTable: " + subtractTable.size());
        for (int i = 1; i < subtractTable.size(); i++) {
            Double product;
            product = SUBTRACT_VALUE(subtrahend, subtractTable.get(i));
            subtractNumbers.add(product);
        }

//        System.out.println("SIZE divisionTable: " + divisionTable.size());
        for (int i = 1; i < divisionTable.size(); i++) {
            logarithmicNumbers = divisionTable(divisionTable, subtractNumbers);
//            logarithmicNumbers.forEach(System.out::println);
        }

//        System.out.println("SIZE logarithmicNumbers: " + logarithmicNumbers.size());
        for (int i = 0; i < logarithmicNumbers.size(); i++) {
            Double getLoc = logarithmicNumbers.get(i);
            Double logLoc = Math.log(getLoc);
            productTable.add(logLoc);
        }

        return productTable;
    }

    public  List<Double> divisionTable(List<Double> factor, List<Double> multiplier) {

        double product;
        System.out.println("f: " + factor.size() + " | m: "+ multiplier.size());
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < factor.size(); i++) {

            product = factor.get(i) / multiplier.get(i);
            productTable.add(product);
//            System.out.println("\n"+i+")");
//            productTable.forEach(System.out::println);
        }
//        System.out.println("\nDIVISION_TABLE:");

        return productTable;
    }

    public  Double MULTIPLY(double multiplier, List<Double> values) {
        Double result = 0.0;

        for (Double value : values)
            result = value * multiplier;
        return result;
    }

    public  List<Double> multiplyList(Double multiplier, List<Double> values) {
        System.out.println("multiplyList:");
        Double number = 0.0;
        List<Double> newList= new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            number = values.get(i);
            BigDecimal newNumber =  BigDecimal.valueOf(multiplier).multiply(BigDecimal.valueOf(number));

            Double finalNumber = Double.parseDouble(String.valueOf(newNumber));
            newList.add(finalNumber);
        }
//        values.forEach(value -> MULTIPLY(multiplier, values));
        newList.forEach(System.out::println);
        System.out.println("====");
        return newList;
    }


    public  double SUM(List<Double> values) {
        Double result = 0.0;
        for (int i = 0; i < values.size(); i++) {
            result += values.get(i);
        }
//        System.out.println("SUM: " + result);
        return result;
    }

    public  double COUNT(List<Double> values) {
//        System.out.println("values.length: " + values.length);
        return values.size();
    }

    public  Double SUM_PRODUCT(List<Double> factor, List<Double> multiplier) {

        Double product;
        List<Double> productTable = new ArrayList<>();
//        System.out.println("SIZE multiplier: " + multiplier.size());
//        System.out.println("SIZE factor: " + factor.size());
        for (int i = 1; i < factor.size(); i++) {

            product = factor.get(i) * multiplier.get(i);
//            System.out.println(product);
            productTable.add(product);
        }

        product = SUM(productTable);
//        System.out.println(product);
        return product;
    }

    public Double SUMSQ(List<Double> values) {
//        System.out.println("SUMSQ:");
        double result = 0;
        for (double value : values) {
            result = Math.pow(value, 2);
        }
//        System.out.println(result);
        return result;
    }

}

