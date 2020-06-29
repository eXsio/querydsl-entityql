package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity;

import com.google.common.base.CaseFormat;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

public class UpperCaseWithUnderscoresNamingStrategy implements NamingStrategy {

    @Override
    public String getTableName(Class<?> type) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type.getSimpleName()) + "S";
    }

    @Override
    public String getColumnName(RelationalPersistentProperty property) {

        String propertyInLowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, property.getName());

        if (property.isIdProperty()) {
            return property.getOwner().getType().getSimpleName().toUpperCase() + "_" + propertyInLowerUnderscore;
        }

        return propertyInLowerUnderscore;
    }

    @Override
    public String getReverseColumnName(PersistentPropertyPathExtension path) {
        return path.getIdColumnName();
    }
}
