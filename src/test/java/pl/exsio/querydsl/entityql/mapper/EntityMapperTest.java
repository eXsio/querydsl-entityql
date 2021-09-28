package pl.exsio.querydsl.entityql.mapper;


import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.types.Null;
import org.junit.Test;
import pl.exsio.querydsl.entityql.jpa.entity.JBook;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.exsio.querydsl.entityql.jpa.entity.generated.QJBook.qJBook;

public class EntityMapperTest {

    @Test
    public void notSearchPropertyIfUsingBeanMapper() throws Exception {
        Long expectedId = 1L;
        JBook obj = new JBook();
        obj.setId(expectedId);

        BeanMapper mapper = BeanMapper.DEFAULT;
        Map<Path<?>, Object> result = mapper.createMap(qJBook, obj);

        assertThat(result.containsValue(expectedId)).isFalse();
    }

    @Test
    public void searchPropertyIfUsingBeanMapper() throws Exception {
        Long expectedId = 1L;
        JBook obj = new JBook();
        obj.setId(expectedId);

        EntityMapper mapper = EntityMapper.DEFAULT;
        Map<Path<?>, Object> result = mapper.createMap(qJBook, obj);

        assertThat(result).containsValue(expectedId);
    }

    @Test
    public void nullBindingIfUsingBeanMapper() throws Exception {
        Long expectedId = 1L;
        JBook obj = new JBook();
        obj.setId(expectedId);

        EntityMapper mapper = EntityMapper.WITH_NULL_BINDINGS;
        Map<Path<?>, Object> result = mapper.createMap(qJBook, obj);

        assertThat(result).containsValue(expectedId);
        assertThat(result).containsValue(Null.DEFAULT);
    }
}
