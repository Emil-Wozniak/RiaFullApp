package org.ria.ifzz.RiaApp.services.examination;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

class ExaminationPointServiceTest {

    private List<ExaminationPoint> examinationPoints;
    private ExaminationPoint examinationPoint1;
    private ExaminationPoint examinationPoint2;
    private ExaminationPoint examinationPoint3;
    private ExaminationPoint examinationPoint4;

    @BeforeEach
    void setUp() {
        examinationPoint1 = createExaminationPoint("A16_244",1,"1",200,false,"0.5");
        examinationPoint2 = createExaminationPoint("A16_244",2,"2",300,false,"1.5");
        examinationPoint3 = createExaminationPoint("A16_244",3,"3",20,true,"15.5");
        examinationPoint4 = createExaminationPoint("A16_245",18,"18",2000,true,"45.5");
        examinationPoints = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        examinationPoints.clear();
    }

    private ExaminationPoint createExaminationPoint(String filename, int probeNumber, String position, int cpm, boolean flag, String ng) {
        return new ExaminationPoint(filename, "KORTYZOL_5_MIN", probeNumber, position, cpm, flag, ng);
    }

    private void fillExaminationPoints(){
        examinationPoints.add(examinationPoint1);
        examinationPoints.add(examinationPoint2);
        examinationPoints.add(examinationPoint3);
        examinationPoints.add(examinationPoint4);
    }

    @Test
    void create_positive() {
        fillExaminationPoints();
        assertFalse(examinationPoints.isEmpty());
    }

    @Test
    void create_negative() {
        assertTrue(examinationPoints.isEmpty());
    }

    @Test
    void getExaminationResults_positive() {
        fillExaminationPoints();
        List<ExaminationPoint> getExaminationPoints = examinationPoints;
        assertNotNull(getExaminationPoints);
        assertFalse(getExaminationPoints.isEmpty());
    }

    @Test
    void getExaminationResults_negative() {
        List<ExaminationPoint> getExaminationPoints = examinationPoints;
        assertNotNull(getExaminationPoints);
        assertTrue(getExaminationPoints.isEmpty());
    }

    @Test
    void getExaminationResultsByFilename_positive() {
        fillExaminationPoints();
        List<String> identifiers = examinationPoints.stream()
                .map(ExaminationPoint::getIdentifier)
                .filter(ex->ex.equals("A16_244")).collect(Collectors.toList());
        assertTrue(identifiers.contains("A16_244"));
    }

    @Test
    void getExaminationResultsByFilename_negative() {
        List<String> identifiers = examinationPoints.stream()
                .map(ExaminationPoint::getIdentifier)
                .filter(ex->ex.equals("A16_244")).collect(Collectors.toList());
        assertFalse(identifiers.contains("A16_244"));
    }
}
