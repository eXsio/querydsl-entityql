package pl.exsio.querydsl.entityql;

import com.google.common.primitives.Primitives;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility Class used to generate Static Java classes from the Dynamic Q Models.
 * For most cases you should rather use the designated Maven plugin available here:
 * https://github.com/eXsio/querydsl-entityql-maven-plugin
 */
public class QExporter {

    private final EnvironmentConfiguration configuration = EnvironmentConfigurationBuilder
            .configuration()
            .functions()
            .add(new PrimitiveWrapper())
            .and()
            .build();

    private final Formatter formatter = new Formatter();

    /**
     * Exports the given Q Model to physical Java source code file.
     *
     * Example:
     *
     * String fileNamePattern = "Q%s.java"; // file/class name pattern
     * String packageName = "com.example.yourpackage"; //package of the generated class
     * String destinationPath = "/some/destination/path"; //physical location of resulting *.java file
     *
     * //this will generate a Java class under "/some/destination/path/com/example/yourpackage/QYourEntity.java"
     * new QExporter().export(qEntity(YourEntity.class), fileNamePattern, packageName, destinationPath);
     *
     * @param q - Q Model to be exported
     * @param fileNamePattern - String patter of File/Class name, for example "Q%s.java"
     * @param pkgName - package name of the resulting class
     * @param destinationPath - physical destination path
     * @throws IOException -  when it is not possible to save the resulting .java file
     * @throws FormatterException - when the resulting Java class is malformed
     */
    public <E> void export(Q<E> q, String fileNamePattern,
                           String pkgName, String destinationPath) throws IOException, FormatterException {

        Class<? extends E> type = q.getType();
        String fileName = String.format(fileNamePattern, type.getSimpleName());
        Path filePath = getFilePath(pkgName, destinationPath, fileName);
        boolean isGroovy = fileName.endsWith("groovy");
        String exportedClass = renderClass(q, pkgName, type, fileName, isGroovy);
        FileUtils.writeStringToFile(
                new File(filePath.toUri()),
                formatter.formatSourceAndFixImports(exportedClass),
                StandardCharsets.UTF_8
        );
    }

    private Path getFilePath(String pkgName, String destinationPath, String fileName) {
        List<String> pathElements = new ArrayList<>(Arrays.asList(pkgName.split("\\.")));
        pathElements.add(fileName);
        return Paths.get(destinationPath, pathElements.toArray(new String[0]));
    }

    private <E> String renderClass(Q<E> q, String pkgName, Class<? extends E> type, String fileName, boolean isGroovy) {
        String className = FilenameUtils.removeExtension(fileName);
        JtwigTemplate template = JtwigTemplate.classpathTemplate("staticTemplate.twig", configuration);
        return template.render(JtwigModel.newModel()
                .with("package", pkgName)
                .with("className", className)
                .with("entityName", type.getName())
                .with("q", q)
                .with("isGroovy", isGroovy)
        );
    }

    public static class PrimitiveWrapper extends SimpleJtwigFunction {

        @Override
        public String name() {
            return "wrapPrimitive";
        }

        @Override
        public Object execute(FunctionRequest functionRequest) {
            return Primitives.wrap((Class<?>) functionRequest.get(0));
        }
    }

}
