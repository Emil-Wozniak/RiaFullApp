package org.ria.ifzz.RiaApp.services.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public interface SpreadCounter {

    Logger LOGGER = LoggerFactory.getLogger(SpreadCounter.class);

    static List<Boolean> isSpread(List<Integer> CPMs) {
        List<Boolean> flagged = new ArrayList<>();
        Integer first = CPMs.get(0), second = CPMs.get(1), third = CPMs.get(2);
        boolean firstFlag = false, secondFlag = false, thirdFlag = false;

        if (first - second != 0 || second - third != 0) {
            if ((first - second) > (second / 10) || (first - third) > (third / 10)) {
                firstFlag = true;
                LOGGER.info(first + " flagged " + true);
            } else if ((second - first) > (first / 10) || (second - third) > (third / 10)) {
                secondFlag = true;
                LOGGER.info(second + " flagged " + true);
            } else if ((third - second) > (second / 10) || (third - first) > (first / 10)) {
                thirdFlag = true;
                LOGGER.info(third + " flagged " + true);
            }
        }
        flagged.add(firstFlag);
        flagged.add(secondFlag);
        flagged.add(thirdFlag);
        return flagged;
    }
}
