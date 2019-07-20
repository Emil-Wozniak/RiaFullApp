package org.ria.ifzz.RiaApp.services.examination;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControlCurveServiceTest {

    private List<ControlCurve> controlCurves;
    private ControlCurve controlCurve1;
    private ControlCurve controlCurve2;
    private ControlCurve controlCurve3;
    private ControlCurve controlCurve4;

    @BeforeEach
    void setUp() {
        controlCurve1 = createControlCurve("A16_244",1,"1",200,false,"0.5");
        controlCurve2 = createControlCurve("A16_244",2,"2",300,false,"1.5");
        controlCurve3 = createControlCurve("A16_244",3,"3",20,true,"15.5");
        controlCurve4 = createControlCurve("A16_245",18,"18",2000,true,"45.5");
        controlCurves = new ArrayList<>();
    }


    @AfterEach
    void tearDown() {
    }

    private ControlCurve createControlCurve(String filename, int probeNumber, String position, int cpm, boolean flag, String ng) {
        return new ControlCurve(filename, "KORTYZOL_5_MIN", probeNumber, position, cpm, flag);
    }

    private void fillControlCurves(){
        controlCurves.add(controlCurve1);
        controlCurves.add(controlCurve2);
        controlCurves.add(controlCurve3);
        controlCurves.add(controlCurve4);
    }

    @Test
    void create_positive() {
        fillControlCurves();
        assertFalse(controlCurves.isEmpty());
    }

    @Test
    void create_negative() {
        assertTrue(controlCurves.isEmpty());
    }

    @Test
    void findAll_positive() {
        fillControlCurves();
        List<String> identifiers = controlCurves.stream()
                .map(ControlCurve::getIdentifier)
                .filter(ex->ex.equals("A16_244")).collect(Collectors.toList());
        Assert.assertTrue(identifiers.contains("A16_244"));
    }

    @Test
    void findAll_negative() {
        List<String> identifiers = controlCurves.stream()
                .map(ControlCurve::getIdentifier)
                .filter(ex->ex.equals("A16_244")).collect(Collectors.toList());
        assertFalse(identifiers.contains("A16_244"));
    }
}
