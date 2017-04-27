package mx.com.aon.portal.dao;

import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.util.WrapperResultados;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.object.StoredProcedure;

@Deprecated
public abstract class CustomStoredProcedure extends StoredProcedure {


    protected CustomStoredProcedure() {
    }

    protected CustomStoredProcedure(DataSource dataSource, String string) {
        super(dataSource, string);
    }

    protected CustomStoredProcedure(JdbcTemplate jdbcTemplate, String string) {
        super(jdbcTemplate, string);
    }

    public abstract WrapperResultados mapWrapperResultados(Map map) throws Exception;
}
