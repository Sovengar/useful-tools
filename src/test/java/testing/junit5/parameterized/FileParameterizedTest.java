package testing.junit5.parameterized;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@DisplayName("JUnit 5: Tests Parametrizados desde Fichero")
class FileParameterizedTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest(name = "Caso de prueba: {0}")
    @MethodSource("testData")
    @DisplayName("Carga dinámica de JSON (.in.json -> .out.json)")
    void fileBasedTest(FileTestCase testCase) throws IOException {
        // IMPORTANTE: Cada iteración de este test corresponde a un PAR de ficheros
        // físicos (.in / .out).
        // No se trata de un único JSON con múltiples entradas, sino de escaneo del
        // classpath.
        // En un caso real aquí llamaríamos al servicio bajo test
        // System.out.println("Procesando: " + testCase.getInput());

        // Simulamos la lógica: convertir mensaje a mayúsculas y poner status PROCESSED
        String inputMessage = testCase.getInput().get("message").asText();
        String processedResult = inputMessage.toUpperCase();

        String actualResponse = """
                {
                    "status": "PROCESSED",
                    "result": "%s"
                }
                """.formatted(processedResult);

        // Validamos contra el fichero .out.json usando JSON-Unit
        assertThatJson(actualResponse)
                .whenIgnoringPaths("extra_fields") // Ejemplo de ignorar campos si fuera necesario
                .isEqualTo(testCase.getExpectedOutput());
    }

    public static List<FileTestCase> testData() throws IOException {
        Function<String, String> inToOutFileName = inputFileName -> inputFileName.replace(".in.json", ".out.json");
        return scanForFileTestCases("classpath:/testing/junit5/parameterized/message/*.in.json", inToOutFileName);
    }

    private static List<FileTestCase> scanForFileTestCases(String pattern, Function<String, String> inToOut)
            throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] inResources = resolver.getResources(pattern);
        List<FileTestCase> testCases = new ArrayList<>();

        for (Resource inResource : inResources) {
            String inFileName = inResource.getFilename();
            String outFileName = inToOut.apply(inFileName);
            Resource outResource = resolver
                    .getResource(inResource.getURL().toString().replace(inFileName, outFileName));

            if (outResource.exists()) {
                JsonNode input = mapper.readTree(inResource.getInputStream());
                JsonNode output = mapper.readTree(outResource.getInputStream());
                testCases.add(new FileTestCase(inFileName, input, output));
            }
        }
        return testCases;
    }

    // Clase DTO para los casos de prueba
    static class FileTestCase {
        private final String name;
        private final JsonNode input;
        private final JsonNode expectedOutput;

        FileTestCase(String name, JsonNode input, JsonNode expectedOutput) {
            this.name = name;
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        public String getName() {
            return name;
        }

        public JsonNode getInput() {
            return input;
        }

        public JsonNode getExpectedOutput() {
            return expectedOutput;
        }

        @Override
        public String toString() {
            return name; // Este es el nombre que aparecerá en el reporte de JUnit
        }
    }
}
