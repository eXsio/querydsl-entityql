package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity;

import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import pl.exsio.querydsl.entityql.util.NamingUtil.camelToUnderscore

class KUpperCaseWithUnderscoresNamingStrategy : NamingStrategy {

    @Override
    override fun getTableName(type: Class<*>): String {
        return (camelToUnderscore(type.simpleName) + "S").replace("K_", "")
    }

    @Override
    override fun getColumnName(property: RelationalPersistentProperty): String {
        val propertyInLowerUnderscore = camelToUnderscore(property.name);
        if (property.isIdProperty()) {
            return property.getOwner().getType().getSimpleName().uppercase() + "_" + propertyInLowerUnderscore;
        }
        return propertyInLowerUnderscore;
    }

    @Override
    override fun getReverseColumnName(path: PersistentPropertyPathExtension): String {
        return path.getIdColumnName().getReference();
    }
}
