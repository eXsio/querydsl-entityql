package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import static pl.exsio.querydsl.entityql.util.NamingUtil.camelToUnderscore;
import static pl.exsio.querydsl.entityql.util.NamingUtil.underscoreToCamel;

public class UnderscoreToCamelStrategyQRuntimeNamingStrategy implements QRuntimeNamingStrategy {

    @Override
    public String getFieldName(String columnName) {
        return underscoreToCamel(columnName);
    }

    @Override
    public String getColumnName(String fieldName) {
        return camelToUnderscore(fieldName);
    }

    public static UnderscoreToCamelStrategyQRuntimeNamingStrategy getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {

        public static UnderscoreToCamelStrategyQRuntimeNamingStrategy INSTANCE = new UnderscoreToCamelStrategyQRuntimeNamingStrategy();

    }

}
