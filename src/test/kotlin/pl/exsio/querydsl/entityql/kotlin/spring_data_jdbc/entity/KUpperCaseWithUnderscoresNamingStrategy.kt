package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity;

import com.google.common.base.CaseFormat;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

class KUpperCaseWithUnderscoresNamingStrategy : NamingStrategy {

    @Override
    override fun getTableName(type: Class<*>): String {
        return (CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type.getSimpleName()) + "S").replace("K_", "");
    }

    @Override
    override fun getColumnName(property: RelationalPersistentProperty): String {
        val propertyInLowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, property.getName());
        if (property.isIdProperty()) {
            return property.getOwner().getType().getSimpleName().toUpperCase() + "_" + propertyInLowerUnderscore;
        }
        return propertyInLowerUnderscore;
    }

    @Override
    override fun getReverseColumnName(path: PersistentPropertyPathExtension): String {
        return path.getIdColumnName();
    }
}
