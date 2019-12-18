package pl.exsio.querydsl.entityql.type;

import com.querydsl.sql.types.AbstractType;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class EnumType<T extends Enum<T>> extends AbstractType<T> {

    private final Class<T> enumType;

    public EnumType(Class<T> enumType) {
        super(Types.VARCHAR);
        this.enumType = enumType;
    }

    @Override
    public Class<T> getReturnedClass() {
        return enumType;
    }

    @Nullable
    @Override
    public T getValue(ResultSet rs, int startIndex) throws SQLException {
        return Enum.valueOf(enumType, rs.getString(startIndex));
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, T value) throws SQLException {
        st.setString(startIndex, value.name());
    }
}
