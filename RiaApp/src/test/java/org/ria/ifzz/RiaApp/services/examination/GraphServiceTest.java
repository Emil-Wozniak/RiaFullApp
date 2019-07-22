package org.ria.ifzz.RiaApp.services.examination;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class GraphServiceTest {

    @Resource
    private EntityManager entityManager;

    private List<GraphLine> graphLines;
    private CountResultUtil countResultUtil;

    @BeforeEach
    void setUp() {
    graphLines = new ArrayList<>();
    countResultUtil = new CountResultUtil();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testDatabase() {
        Query query = entityManager.createNativeQuery("SELECT 1");
        assertEquals(BigInteger.valueOf(1L), query.getSingleResult());
    }

    @Test
    void create() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllLines() {
    }
}
