package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import com.google.common.base.CaseFormat;

public class UnderscoreToCamelStrategy implements NamingStrategy {

    @Override
    public String getFieldName(String columnName) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
    }

    @Override
    public String getColumnName(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName);
    }

    public static UnderscoreToCamelStrategy getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {

        public static UnderscoreToCamelStrategy INSTANCE = new UnderscoreToCamelStrategy();

    }

}
