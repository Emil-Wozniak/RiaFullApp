package org.ria.ifzz.RiaApp.utils.counter;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * performs mathematical algorithms
 */
public interface Algorithms {

    static double count(List<Double> values) {
        return values.size();
    }

    static List<Double> subtractTableElements(Double subtrahend, List<Double> values) {
        return values.stream().map(element -> (subtrahend - element)).collect(Collectors.toList());
    }

    static List<Double> subtractTablesElement(List<Integer> subtrahends, double value) {
        return subtrahends.stream().map(Double::valueOf).map(subtrahend -> subtrahend - value).collect(Collectors.toList());
    }

    static List<Double> divideTableCeilElements(double factor, List<Double> values) {
        return values
                .stream()
                .map(element -> (element / factor))
                .map(element -> round(element, 1))
                .map(element -> Precision.round(element, 0)).collect(Collectors.toList());
    }

    static List<Double> logarithmTable2(List<Double> logarithmTable) {
        List<Double> resultTable;
        resultTable = logarithmTable
                .stream()
                .map(java.lang.Math::log10)
                .map(element -> Precision.round(element, 3))
                .map(element -> round(element, 2))
                .collect(Collectors.toList());
        return resultTable;
    }

    static List<Double> logarithmTable1(List<Double> logarithmTable) {
        List<Double> resultTable;
        resultTable = logarithmTable.stream().map(element -> round(element, 1)).collect(Collectors.toList());
        return resultTable;
    }

    static List<Double> divisionTable(List<Double> factors, List<Double> values) {
        double product;
        List<Double> productTable = new ArrayList<>();
        for (int i = 0; i < factors.size(); i++) {
            product = factors.get(i) / values.get(i);
            product = Precision.round(product, 2);
            productTable.add(product);
        }
        return productTable;
    }

    static List<Double> multiplyList(Double multiplier, List<Double> values) {
        return values.stream().map(element -> (element * multiplier)).collect(Collectors.toList());
    }

    static double sum(List<Double> values) {
        double product = values.stream().mapToDouble(value -> Precision.round(value, 2)).sum();
        product = Precision.round(product, 2);
        return product;
    }

    static Double sumProduct(List<Double> factor, List<Double> multiplier) {
        List<Pair<Double, Double>> pairs = new ArrayList<>();
        for (int i = 0; i < multiplier.size(); i++) {
            pairs.add(new Pair<>(factor.get(i),multiplier.get(i)));
        }
        double product;
        product = pairs.parallelStream().mapToDouble(p -> p.getFirst() * p.getSecond()).map(element -> Precision.round(element, 1)).sum();
        product = Precision.round(product, 3);
        return product;
    }

    static Double sumsq(List<Double> values) {
        return values.parallelStream().mapToDouble(element -> java.lang.Math.pow(element, 2)).sum();
    }

    /**
     * {@link //stackoverflow.com/users/56285/jonik }
     *
     * @param value  digit that will be rounded
     * @param places where rounded will be preformed
     * @return digit rounded in pointed place
     * @author Jonik
     */
    static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    static double getPointSubtract(double first_value, double second_value) {
        return first_value - second_value;
    }
}

