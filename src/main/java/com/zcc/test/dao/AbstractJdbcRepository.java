package com.zcc.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 15-6-13
 *
 * @author Shengzhao Li
 */
public abstract class AbstractJdbcRepository {


    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
