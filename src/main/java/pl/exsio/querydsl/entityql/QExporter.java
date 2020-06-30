package pl.exsio.querydsl.entityql;

import com.facebook.ktfmt.FormatterKt;
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
import java.lang.reflect.ParameterizedType;
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
            .newLineTrimming(false)
            .build();

    private final Formatter javaFormatter = new Formatter();

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
        Lang lang = Lang.forName(fileName);
        String exportedClass = renderClass(q, pkgName, type, fileName, lang);
        FileUtils.writeStringToFile(
                new File(filePath.toUri()),
                format(lang, exportedClass),
                StandardCharsets.UTF_8
        );
    }

    private String format(Lang lang, String exportedClass) throws FormatterException {
        return lang.equals(Lang.KOTLIN) ? FormatterKt.format(exportedClass) : javaFormatter.formatSourceAndFixImports(exportedClass);
    }

    private Path getFilePath(String pkgName, String destinationPath, String fileName) {
        List<String> pathElements = new ArrayList<>(Arrays.asList(pkgName.split("\\.")));
        pathElements.add(fileName);
        return Paths.get(destinationPath, pathElements.toArray(new String[0]));
    }

    private <E> String renderClass(Q<E> q, String pkgName, Class<? extends E> type, String fileName, Lang lang) {
        String className = FilenameUtils.removeExtension(fileName);
        PebbleTemplate template = engine.getTemplate(lang.getTemplateName());
        Map<String, Object> context = getContext(q, pkgName, type, lang, className);
        return doRender(template, context)
                .replaceAll("\\r\\n", "\n")
                .replaceAll("\\r", "\n");
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

    private <E> Map<String, Object> getContext(Q<E> q, String pkgName, Class<? extends E> type, Lang lang, String className) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("package", pkgName);
        context.put("className", className);
        context.put("entityName", type.getName());
        context.put("entitySimpleName", type.getSimpleName());
        context.put("entityClass", type);
        context.put("exporterName", getClass().getName());
        context.put("uid", getHash(q));
        context.put("q", q);
        context.put("idCols", q.idColumns);
        context.put("lang", lang);
        context.put("isGroovy", lang.equals(Lang.GROOVY));
        return context;
    }

    private <E> int getHash(Q<E> q) {
        return Objects.hash(q.columns().keySet(), q.joinColumns().keySet(), q.inverseJoinColumns().keySet());
    }

    public static class EntityQlExtension extends AbstractExtension {

        @Override
        public Map<String, Function> getFunctions() {
            Map<String, Function> functions = Maps.newHashMap();
            functions.put("wrapPrimitive", new PrimitiveWrapper());
            functions.put("isNotJavaLang", new IsNotJavaLang());
            functions.put("isParametrized", new IsParametrized());
            functions.put("capitalize", new Capitalize());
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

    public static class IsNotJavaLang implements Function {

        @Override
        public List<String> getArgumentNames() {
            List<String> names = new ArrayList<>();
            names.add("target");
            return names;
        }

        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            Class<?> target = (Class<?>) args.get("target");
            return !target.getName().startsWith("java.lang") && !target.isPrimitive();
        }
    }

    public static class IsParametrized implements Function {

        @Override
        public List<String> getArgumentNames() {
            List<String> names = new ArrayList<>();
            names.add("target");
            return names;
        }

        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            return ((Class<?>) args.get("target")).getTypeParameters().length > 0;
        }
    }

    public static class Capitalize implements Function {

        @Override
        public List<String> getArgumentNames() {
            List<String> names = new ArrayList<>();
            names.add("target");
            return names;
        }

        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String target = (String) args.get("target");
            return target.substring(0, 1).toUpperCase() + target.substring(1);
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
            return target.replace(toReplace, replaceWith);
        }
    }

    private enum Lang {
        JAVA("staticTemplate.peb"),
        GROOVY("staticTemplate.peb"),
        KOTLIN("staticTemplateKt.peb");

        private final String templateName;

        Lang(String templateName) {
            this.templateName = templateName;
        }

        public String getTemplateName() {
            return templateName;
        }

        private static Lang forName(String fileName) {
            if (fileName.endsWith("kt")) {
                return KOTLIN;
            } else if (fileName.endsWith("groovy")) {
                return GROOVY;
            } else {
                return JAVA;
            }
        }
    }

}
