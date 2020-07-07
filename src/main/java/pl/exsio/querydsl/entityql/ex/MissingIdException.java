package pl.exsio.querydsl.entityql.ex;

public class MissingIdException extends RuntimeException {

    public MissingIdException(Class<?> entityClass) {
        super(String.format("Entity Class '%s' must have a field annotated with @Id and @Column",
                entityClass.getName()));
    }

    public MissingIdException(String tableName) {
        super(String.format("Table '%s' must have a primary key", tableName));
    }

}
