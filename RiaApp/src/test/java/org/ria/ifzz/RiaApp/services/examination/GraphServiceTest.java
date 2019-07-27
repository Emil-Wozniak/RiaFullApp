package org.ria.ifzz.RiaApp.services.examination;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;
import org.ria.ifzz.RiaApp.models.graph.Graph;
import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ria.ifzz.RiaApp.utils.CustomFileReader.readFromStream;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@ActiveProfiles("test")
//@Transactional
class GraphServiceTest implements CustomFileReader {

    @Resource
    private EntityManager entityManager;

    CountResultUtil countResultUtil;
    private List<GraphLine> graphLines;
    private List<String> metadata;
    private Graph graph;

    @BeforeEach
    void setUp() throws IOException {
        getFileContents();
        graphLines = new ArrayList<>();
        double correlation = 0.986814;
        double zeroBindingPercentage = 21.0;
        double regressionParameterB = -1.0575;
        String filename = "A16_244.txt";
        graph = new Graph(filename, correlation, zeroBindingPercentage, regressionParameterB);
    }

    @AfterEach
    void tearDown() {
        metadata.clear();
    }

    private void getFileContents() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("example/A16_244.txt").getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        DataFileMetadata metadata = new DataFileMetadata(multipartFile);
        this.metadata = readFromStream(metadata);
    }


    @Test
    void testEmbeddedPg() throws Exception {
        try (EmbeddedPostgres pg = EmbeddedPostgres.start();
             Connection connection = pg.getPostgresDatabase().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1");
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
            assertFalse(resultSet.next());
        } catch (Exception error) {
            System.out.print(error.getMessage());
            fail();
        }
    }

    @Test
    void create_graph() {
        assertNotNull(graph);
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllLines() {
    }
}
