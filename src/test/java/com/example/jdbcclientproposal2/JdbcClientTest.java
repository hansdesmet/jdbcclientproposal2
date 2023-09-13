package com.example.jdbcclientproposal2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql(statements = "insert into persons(name) values ('Joe'), ('Jack'), ('Luke')")
class JdbcClientTest {
    @Autowired
    private DataSource dataSource;

    private record Person(long id, String name) {}

    @Test
    void worksWithNamedParamters() {
        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        List<Person> persons=jdbcClient.sql("select id,name from persons where id in (:ids)")
                .param("ids", List.of(1,3))
                .query(Person.class)
                        .list();
    }

    @Test
    void doesNotWorkWithPositionalParamters() {
        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        List<Person> persons=jdbcClient.sql("select id,name from persons where id in (?)")
                .param(List.of(1,3))
                .query(Person.class)
                .list();

    }
}
