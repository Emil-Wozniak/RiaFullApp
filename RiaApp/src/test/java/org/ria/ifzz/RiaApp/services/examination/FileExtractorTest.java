package org.ria.ifzz.RiaApp.services.examination;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getMatchingString;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.readFromStream;

//@RunWith(SpringRunner.class)
//@SpringBootTest
class FileExtractorTest implements CustomFileReader {

    private List<Integer> probeNumbers;
    private List<String> fileContent;
    private List<ControlCurve> controlCurves;

    @BeforeEach
    void setUp() {
        fileContent = new ArrayList<>();
        probeNumbers = new ArrayList<>();
        fileContent = new ArrayList<>();
        ControlCurve controlCurve1 = new ControlCurve("A16_244.txt", "KORTYZOL_5_MIN", 1, "Total", 49, true);
        ControlCurve controlCurve2 = new ControlCurve("A16_244.txt", "KORTYZOL_5_MIN", 1, "Total", 32, false);
        ControlCurve controlCurve3 = new ControlCurve("A16_244.txt", "KORTYZOL_5_MIN", 1, "Total", 36, false);
        ControlCurve controlCurve4 = new ControlCurve("A16_244.txt", "KORTYZOL_5_MIN", 1, "Total", 458, false);
        ControlCurve controlCurve5 = new ControlCurve("A16_244.txt", "KORTYZOL_5_MIN", 1, "Total", 459, false);
        ControlCurve controlCurve6 = new ControlCurve("A16_244.txt", "KORTYZOL_5_MIN", 1, "Total", 447, false);
        controlCurves = new ArrayList<>();
        controlCurves.add(null);
        controlCurves.add(null);
        controlCurves.add(controlCurve1);
        controlCurves.add(controlCurve2);
        controlCurves.add(controlCurve3);
        controlCurves.add(controlCurve4);
        controlCurves.add(controlCurve5);
        controlCurves.add(controlCurve6);
    }

    @AfterEach
    void tearDown() {
        probeNumbers.clear();
        fileContent.clear();
        controlCurves.clear();
    }

    private List<String> getFileContents() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("example/A16_244.txt").getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        DataFileMetadata metadata = new DataFileMetadata(multipartFile);
        fileContent = readFromStream(metadata);
        return fileContent;
    }

    private List<Integer> getCPMs() throws IOException {
        return getFileContents().stream().skip(2).map(line -> {
            line = getMatchingString(line, 3);
            return line;
        }).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Test
    void setProbeNumber() {
        int to = 10;
        probeNumbers = IntStream.rangeClosed(1, to).boxed().collect(Collectors.toList());
        assertEquals(10, probeNumbers.size());
    }

    @Test
    void setCPMs() throws IOException {
        List<Integer> CPMs = getFileContents().stream().skip(2).map(line -> {
            line = getMatchingString(line, 3);
            return line;
        }).map(Integer::parseInt).collect(Collectors.toList());
        assertTrue(CPMs.contains(1976));
    }

    @Test
    void getPattern() throws IOException {
        getFileContents();
        String pattern = fileContent.get(1);
        assertEquals(pattern, "KORTYZOL_5_MIN");
    }

    @Test
    void setFlag() throws IOException {
        List<Integer> CPMs = getCPMs();
        List<Integer> points = FileExtractor.setProbeNumber(CPMs.size());
        List<Boolean> NSB = FileExtractor.getZeroOrNsbFlag(CPMs, 2, 4);
        List<Boolean> Zeros = FileExtractor.getZeroOrNsbFlag(CPMs, 5, 7);
        List<Boolean> flagged = points.stream().limit(8).map(point -> {
            switch (point) {
                case 1:
                case 2:
                    return false;
                case 3:
                case 4:
                case 5:
                    return NSB.get(FileExtractor.setPoint(point, 3));
                case 6:
                case 7:
                case 8:
                    return Zeros.get(FileExtractor.setPoint(point, 6));
                default:
                    throw new IllegalStateException("Unexpected value: " + point);
            }
        }).collect(Collectors.toList());
        assertEquals(false, flagged.get(0));
        assertEquals(true, flagged.get(2));
        assertEquals(false, flagged.get(5));
        assertEquals(false, flagged.get(7));
    }

    @Test
    void setPoint() throws IOException {
        List<Integer> CPMs = getCPMs();
        List<Integer> points = FileExtractor.setProbeNumber(CPMs.size());
        List<Integer> setterPoints = points.stream().limit(8).map(point -> {
            switch (point) {
                case 3:
                case 4:
                case 5:
                    return point - 3;
                case 6:
                case 7:
                case 8:
                    return point - 6;
                default:
                    return 0;
            }
        }).collect(Collectors.toList());
        int target1 = setterPoints.get(2);
        int target2 = setterPoints.get(5);
        assertEquals(0, target1);
        assertEquals(0, target2);
    }

    @Test
    void getZeroOrNsbFlag() throws IOException {
        List<Integer> CPMsNSB = getCPMs();
        List<Integer> zeroOrNsbPointsNSBs = CPMsNSB.stream().skip(2).limit(3).collect(Collectors.toList());

        List<Integer> CPMsZeros = getCPMs();
        List<Integer> zeroOrNsbPointsZeros = CPMsZeros.stream().skip(5).limit(3).collect(Collectors.toList());

        assertEquals(3, zeroOrNsbPointsNSBs.size());
        assertEquals(3, zeroOrNsbPointsZeros.size());
    }

    @Test
    void checkNSBsOrZeros() {
    }

    @Test
    void getPatternFlags() throws IOException {
        List<Integer> CPMs = getCPMs();
        List<Boolean> flagged = new ArrayList<>();
        double nsb1 = CPMs.get(5), nsb2 = CPMs.get(6), nsb3 = CPMs.get(7);
        for (int element = 8; element < CPMs.size(); element++) {
            int evalPoint = CPMs.get(element);
            if (evalPoint > nsb1 || evalPoint > nsb2 || evalPoint > nsb3) {
                flagged.add(true);
            } else {
                flagged.add(false);
            }
        }
        assertEquals(false, flagged.get(0));
        assertEquals(true, flagged.get(14));
    }

    @Test
    void flagCondition() throws IOException {
        List<Integer> CPMs = getCPMs();
        int cpm = CPMs.get(10);
        boolean isOutOfCurveBoundary = (cpm < controlCurves.get(2).getCpm() || cpm < controlCurves.get(3).getCpm() || cpm < controlCurves.get(4).getCpm()) || (cpm > controlCurves.get(5).getCpm() || cpm > controlCurves.get(6).getCpm() || cpm > controlCurves.get(7).getCpm());
        assertFalse(isOutOfCurveBoundary);
    }
}
