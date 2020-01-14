package pl.exsio.querydsl.entityql;

import com.querydsl.core.types.Path;

/**
 * EntityQL Wrapper for QueryDSL's Path. Contains the Path itself, information about whether the Path is a Parametrized
 * Type and a Config that is needed to construct that Path using QPathFactory::create method.
 */
public class QPath {

    private final Path<?> path;

    private final Class<?> parametrizedType;

    private final QPathConfig config;

    QPath(Path<?> path, QPathConfig config, Class<?> parametrizedType) {
        this.path = path;
        this.parametrizedType = parametrizedType;
        this.config = config;
    }

    QPath(Path<?> path, QPathConfig config) {
        this.path = path;
        this.config = config;
        this.parametrizedType = null;
    }

    public Path<?> get() {
        return path;
    }

    public Class<?> getParametrizedType() {
        return parametrizedType;
    }

    public boolean isParametrized() {
        return parametrizedType != null;
    }

    public QPathConfig getConfig() {
        return config;
    }
}
