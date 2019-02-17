package org.ria.ifzz.RiaApp.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class ResultMath {

    public double count(List<Double> values) {
        System.out.println("values.size: " + values.size());
        return values.size();
    }

    public Double averageTwo(Double first, Double second) {
        return (first + second) / 2;
    }

    public double averageThree(double first, double second, double third) {
        double result = (first + second + third) / 3;
        System.out.println("averageThree: " + result);
        return result;
    }

    public List<Double> subtractTableElements(Double subtrahend, List<Double> values) {
        List<Double> result = new ArrayList<>();
        for (Double value : values) {
            result.add(subtrahend - value);
        }
        System.out.println("subtractTableElements: ");
        result.forEach(System.out::println);
        return result;
    }

    public List<Double> subtractTablesElement(List<Double> subtrahend,  double value) {
        Double sub;
        Double result;
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < subtrahend.size(); i++) {
            sub = subtrahend.get(i);
            result = sub - value;
            results.add(result);
        }
        results.forEach(System.out::println);
        return results;
    }

    public List<Double> divideTableElements(double factor, List<Double> values) {

        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {

            product = values.get(i) / factor;
            productTable.add(product);
        }
        System.out.println("\ndivideTableElements:");
        productTable.forEach(System.out::println);
        return productTable;
    }

    public List<Double> logarithmTable(List<Double> logarithmTable) {
        List<Double> resultTable = new ArrayList<>();
        resultTable = logarithmTable.stream().map(Math::log10).collect(Collectors.toList());
        System.out.println("\nLogarithm Table:");
        resultTable.forEach(System.out::println);
        return resultTable;
    }

    public List<Double> logarithmTables(List<Double> divisionTable, List<Double> subtractTable) {

        List<Double> productTable = new ArrayList<>();
        List<Double> subtractNumbers = new ArrayList<>();
        List<Double> logarithmicNumbers = new ArrayList<>();

        for (int i = 1; i < divisionTable.size(); i++) {
            logarithmicNumbers = divisionTable(divisionTable, subtractNumbers);
        }

        for (int i = 0; i < logarithmicNumbers.size(); i++) {
            Double getLoc = logarithmicNumbers.get(i);
            Double logLoc = Math.log(getLoc);
            productTable.add(logLoc);
        }

        return productTable;
    }

    public List<Double> divisionTable(List<Double> factor, List<Double> values) {

        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < factor.size(); i++) {

            product = factor.get(i) / values.get(i);
            productTable.add(product);
        }
        System.out.println("\nDivision Table:");
        productTable.forEach(System.out::println);
        return productTable;
    }

    public List<Double> multiply(List<Double>  multiplier, List<Double> values) {
        List<Double>  result = new ArrayList<>();
        Double first = 0.0;
        Double second = 0.0;
        for (int i = 0; i < values.size(); i++) {
            first = values.get(i);
            second = multiplier.get(i);
            Double sum = first * second;
            result.add(sum);
        }
        return result;
    }

    public List<Double> multiplyList(Double multiplier, List<Double> values) {
        System.out.println("\nMultiply List:");
        Double number = 0.0;
        List<Double> newList = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            number = values.get(i);
            BigDecimal newNumber = BigDecimal.valueOf(multiplier).multiply(BigDecimal.valueOf(number));

            Double finalNumber = Double.parseDouble(String.valueOf(newNumber));
            newList.add(finalNumber);
        }
        newList.forEach(System.out::println);
        return newList;
    }


    public double sum(List<Double> values) {
        double result = 0.0;

        double[] target = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            target[i] = values.get(i).doubleValue();
        }

        result = DoubleStream.of(target).sum();
        System.out.println("Sum result: " + result);
        return result;
    }

    public Double sumProduct(List<Double> factor, List<Double> multiplier) {

        Double product = 0.0;
        List<Double> multiplyTable = new ArrayList<>();
        multiplyTable =multiply(multiplier,factor);

        product = sum(multiplyTable);
        System.out.println("sumProduct: " + product);
        return product;
    }

    public Double sumsq(List<Double> values) {
        double result = 0.0;
        List<Double> powerValues = new ArrayList<>();

        for (double value : values) {
            result = Math.pow(value, 2);
            powerValues.add(result);
        }
        result = sum(powerValues);

        System.out.println("sumsq: " + result);
        return result;
    }

}

