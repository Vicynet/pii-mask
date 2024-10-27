package dev.vicynet.piimask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MaskDataSerializerTest {
    private ObjectMapper objectMapper;
    private TestPIIModel testPIIModel;
    private static final Logger logger = Logger.getLogger(String.valueOf(MaskDataSerializerTest.class));


    @BeforeEach
    void setUp() {
        testPIIModel = new TestPIIModel();
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(TestPIIModel.class, new MaskDataSerializer());
        objectMapper.registerModule(simpleModule);
    }

    @AfterEach
    void tearDown() {
        objectMapper = null;
        testPIIModel = null;
    }

    @Test
    public void testMaskingSerialization() throws JsonProcessingException {
        String jsonResult = objectMapper.writeValueAsString(testPIIModel);
        logger.log(Level.INFO, jsonResult);
        assertTrue(jsonResult.contains("*****345678"));
        assertTrue(jsonResult.contains("ihedioha@gmail****"));
        assertTrue(jsonResult.contains("807******3453"));
        assertTrue(jsonResult.contains("**34-5678-9876-54321234-5678-9876-54**"));
    }
}