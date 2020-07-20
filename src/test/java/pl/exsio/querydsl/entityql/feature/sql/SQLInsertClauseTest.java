package pl.exsio.querydsl.entityql.feature.sql;

import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.BeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import pl.exsio.querydsl.entityql.EntityQL;
import pl.exsio.querydsl.entityql.Q;
import pl.exsio.querydsl.entityql.dto.BookDto;
import pl.exsio.querydsl.entityql.feature.EntityQLQueryFeature;
import pl.exsio.querydsl.entityql.jpa.entity.JBook;
import pl.exsio.querydsl.entityql.jpa.entity.generated.QJBook;
import pl.exsio.querydsl.entityql.mapper.EntityMapper;

import static com.querydsl.core.types.Projections.constructor;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.exsio.querydsl.entityql.jpa.entity.generated.QJBook.qJBook;

public class SQLInsertClauseTest extends EntityQLQueryFeature {
    private SQLQueryFactory queryFactory;

    @Before
    public void setUp() throws Exception {
        queryFactory = queryFactory();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void throwViolationExceptionIfUsingBeanMapper()  {
        Long expectedId = 1L;
        JBook target = new JBook();
        target.setId(expectedId);

        queryFactory.insert(qJBook)
                .populate(target, BeanMapper.DEFAULT)
                .execute();

    }

    @Test
    public void successInsertIfUsingEntityMapper() throws Exception {
        Long expectedId = 1L;
        JBook target = new JBook();
        target.setId(expectedId);

        queryFactory.insert(qJBook)
                .populate(target, EntityMapper.DEFAULT)
                .execute();

        BookDto result = queryFactory
                .select(constructor(BookDto.class,
                        qJBook.longNumber("id"),
                        qJBook.string("name"),
                        qJBook.string("desc"),
                        qJBook.decimalNumber("price")
                ))
                .from(qJBook)
                .fetchOne();

        assertThat(result.getId()).isEqualTo(expectedId);
    }
}
