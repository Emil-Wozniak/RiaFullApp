package org.ria.ifzz.RiaApp.services.examination;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.*;

class FileExtractorTest implements CustomFileReader {

    private List<Integer> probeNumbers;
    private List<String> fileContent;
    private String line1;
    private String line2;
    private String line3;
    private String line4;

    @BeforeEach
    void setUp() {
        fileContent = new ArrayList<>();
        probeNumbers = new ArrayList<>();
        line1 = " \tUnk_1\tA01\t1976\t2.0\t\n";
        line2 = " \tUnk_2\tA02\t1982\t2.0\t\n";
        line3 = " \tUnk_3\tA03\t49\t12.8\t\n";
        line4 = " \tUnk_4\tA04\t32\t15.7\t\n";
    }

    @AfterEach
    void tearDown() {
        probeNumbers.clear();
    }

    void fillFileContent(){
        fileContent.add(line1);
        fileContent.add(line2);
        fileContent.add(line3);
        fileContent.add(line4);
    }

    @Test
    void setProbeNumber() {
        int to = 10;
        probeNumbers = IntStream.rangeClosed(1, to).boxed().collect(Collectors.toList());
        assertEquals(10, probeNumbers.size());
    }

    @Test
    void setCPMs() {
        fillFileContent();
        List<Integer> CPMs = fileContent.stream().map(line->{
            line= getMatchingString(line,3);
            return line;}).map(Integer::parseInt).collect(Collectors.toList());
        assertTrue(CPMs.contains(1976));
    }

    @Test
    void getPattern() {
    }

    @Test
    void setFlag() {
    }

    @Test
    void setPoint() {
    }

    @Test
    void getZeroOrNsbFlag() {
    }

    @Test
    void checkNSBsOrZeros() {
    }

    @Test
    void getPatternFlags() {
    }

    @Test
    void flagCondition() {
    }
}
