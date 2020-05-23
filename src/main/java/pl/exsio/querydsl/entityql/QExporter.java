package pl.exsio.querydsl.entityql;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import pl.exsio.querydsl.entityql.ex.EntityQlExportException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility Class used to generate Static Java classes from the Dynamic Q Models.
 * For most cases you should rather use the designated Maven plugin available here:
 * https://github.com/eXsio/querydsl-entityql-maven-plugin
 */
public class QExporter {

    private final PebbleEngine engine = new PebbleEngine.Builder()
            .extension(new EntityQlExtension())
            .allowUnsafeMethods(true)
            .build();

    private final Formatter formatter = new Formatter();

    /**
     * Exports the given Q Model to physical Java source code file.
     * <p>
     * Example:
     * <p>
     * String fileNamePattern = "Q%s.java"; // file/class name pattern
     * String packageName = "com.example.yourpackage"; //package of the generated class
     * String destinationPath = "/some/destination/path"; //physical location of resulting *.java file
     * <p>
     * //this will generate a Java class under "/some/destination/path/com/example/yourpackage/QYourEntity.java"
     * new QExporter().export(qEntity(YourEntity.class), fileNamePattern, packageName, destinationPath);
     *
     * @param q               - Q Model to be exported
     * @param fileNamePattern - String patter of File/Class name, for example "Q%s.java"
     * @param pkgName         - package name of the resulting class
     * @param destinationPath - physical destination path
     * @throws IOException        -  when it is not possible to save the resulting .java file
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
        PebbleTemplate template = engine.getTemplate("staticTemplate.peb");

        int hash = Objects.hash(q.columns().keySet(), q.joinColumns().keySet(), q.inverseJoinColumns().keySet());
        Map<String, Object> context = getContext(q, pkgName, type, isGroovy, className, hash);
        return doRender(template, context);
    }

    private String doRender(PebbleTemplate template, Map<String, Object> context) {
        Writer writer = new StringWriter();
        try {
            template.evaluate(writer, context);
        } catch (Exception ex) {
            throw new EntityQlExportException(ex);
        }
        return writer.toString();
    }

    private <E> Map<String, Object> getContext(Q<E> q, String pkgName, Class<? extends E> type, boolean isGroovy, String className, int hash) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("package", pkgName);
        context.put("className", className);
        context.put("entityName", type.getName());
        context.put("entitySimpleName", type.getSimpleName());
        context.put("exporterName", getClass().getName());
        context.put("uid", hash);
        context.put("q", q);
        context.put("idCols", q.idColumns);
        context.put("isGroovy", isGroovy);
        return context;
    }

    public static class EntityQlExtension extends AbstractExtension {

        @Override
        public Map<String, Function> getFunctions() {
            Map<String, Function> functions = Maps.newHashMap();
            functions.put("wrapPrimitive", new PrimitiveWrapper());
            functions.put("replace", new Replace());
            return functions;
        }
    }

    public static class PrimitiveWrapper implements Function {

        @Override
        public List<String> getArgumentNames() {
            List<String> names = new ArrayList<>();
            names.add("target");
            return names;
        }

        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            return Primitives.wrap((Class<?>) args.get("target"));
        }
    }

    public static class Replace implements Function {

        @Override
        public List<String> getArgumentNames() {
            return Lists.newArrayList("target", "toReplace", "replaceWith");
        }

        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String target = (String) args.get("target");
            String toReplace = (String) args.get("toReplace");
            String replaceWith = (String) args.get("replaceWith");
            return target.replaceAll(toReplace, replaceWith);
        }
    }

}
