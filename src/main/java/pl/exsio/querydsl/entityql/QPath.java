package pl.exsio.querydsl.entityql;

import com.querydsl.core.types.Path;

public class QPath {

    private final Path<?> path;

    private final Class<?> parametrizedType;

    QPath(Path<?> path, Class<?> parametrizedType) {
        this.path = path;
        this.parametrizedType = parametrizedType;
    }

    QPath(Path<?> path) {
        this.path = path;
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
}
