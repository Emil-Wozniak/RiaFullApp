package org.ria.ifzz.RiaApp.utils;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultMath {

    public double count(List<Double> values) {
        System.out.println("values.size: " + values.size());
        return values.size();
    }

    public Double averageTwo(Double first, Double second) {
        double result = (first + second) / 2;
        double ceil = Math.ceil(result);
        return ceil;
    }

    public double averageThree(double first, double second, double third) {
        double result = (first + second + third) / 3;
        Math.ceil(result);
        double ceil = Math.ceil(result);
        System.out.println("Average three: " + ceil);
        return ceil;
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

    public List<Double> subtractTablesElement(List<Double> subtrahend, double value) {
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

    public List<Double> divideTableCeilElements(double factor, List<Double> values) {

        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {

            product = values.get(i) / factor;
            product = Math.ceil(product);
            productTable.add(product);
        }
        System.out.println("\nDivide Table Ceil Elements:");
        productTable.forEach(System.out::println);
        return productTable;
    }

    public List<Double> logarithmTable2(List<Double> logarithmTable) {
        List<Double> resultTable;
        resultTable = logarithmTable.stream().map(Math::log10).map(element -> Precision.round(element, 2)).collect(Collectors.toList());
        System.out.println("\nLogarithm Table:");
        resultTable.forEach(System.out::println);
        return resultTable;
    }

    public List<Double> logarithmTable3(List<Double> logarithmTable) {
        List<Double> resultTable;
        resultTable = logarithmTable.stream()
                .map(Math::log10)
                .map(element -> Precision.round(element, 3))
                .collect(Collectors.toList());
        System.out.println("\nLogarithm Table precision 3:");
        resultTable.forEach(System.out::println);
        return resultTable;
    }


    public List<Double> divisionTable(List<Double> factor, List<Double> values) {

        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < factor.size(); i++) {
            product = factor.get(i) / values.get(i);
            product = Precision.round(product, 2);
            productTable.add(product);
        }
        System.out.println("\nDivision Table:");
        productTable.forEach(System.out::println);
        return productTable;
    }

    public List<Double> multiplyList(Double multiplier, List<Double> values) {
        System.out.println("\nMultiply List:");
        Double number;
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
        Double product = values.stream().mapToDouble(value -> Precision.round(value, 2)).sum();
        product = Precision.round(product, 2);
        System.out.println("Sum result: " + product);
        return product;
    }
    public double sum4(List<Double> values) {
        Double product = values.stream()
                .mapToDouble(value -> value)
                .sum();
        product = Precision.round(product, 3);
        System.out.println("Sum4 result: " + product);
        return product;
    }

    public Double sumProduct(List<Double> factor, List<Double> multiplier) {

        double product;
        List<Pair<Double, Double>> pairs = new ArrayList<>();
        for (int i = 0; i < multiplier.size(); i++) {
            pairs.add(new Pair<>(factor.get(i), multiplier.get(i)));
        }
        product = pairs.parallelStream()
                .mapToDouble(p -> p.getFirst() * p.getSecond())
                .map(element -> Precision.round(element, 1)).sum();
        product = Precision.round(product, 3);

        System.out.println("sumProduct: " + product);
        return product;
    }

    public Double sumsq(List<Double> values) {
        double result;
        List<Double> powerValues = new ArrayList<>();

        for (double value : values) {
            result = Math.pow(value, 2);
            powerValues.add(result);
        }
        result = sum(powerValues);

        System.out.println("sumsq: " + result);
        return result;
    }

    public List<Double> roundAvoid(List<Double> values) {
        List<Double> result;
        result = values.stream()
                .map(element -> element * 10)
                .map(Double::longValue)
                .map(element -> element / 10D)
                .map(element -> Precision.round(element, 2))
                .collect(Collectors.toList());
        return result;
    }
}

