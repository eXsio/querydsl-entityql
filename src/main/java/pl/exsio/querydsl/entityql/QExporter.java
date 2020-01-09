package pl.exsio.querydsl.entityql;

import com.google.googlejavaformat.java.Formatter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class QExporter {

    private final Formatter formatter = new Formatter();

    public <E> void export(Q<E> q, String fileNamePattern,
                           String pkgName, String destinationPath) throws Exception {

        Class<? extends E> type = q.getType();
        String fileName = String.format(fileNamePattern, type.getSimpleName());
        String className = FilenameUtils.removeExtension(fileName);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("staticTemplate.twig");
        String renderedTemplate = template.render(JtwigModel.newModel()
                .with("package", pkgName)
                .with("className", className)
                .with("entity", type.getName())
                .with("q", q)
        );

        FileUtils.writeStringToFile(new File(Paths.get(destinationPath, fileName).toUri()), formatter.formatSource(renderedTemplate), StandardCharsets.UTF_8);

    }

}
