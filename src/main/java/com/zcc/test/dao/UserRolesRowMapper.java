/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.zcc.test.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zcc.test.model.UserRoles;

/**
 * 2015/10/26
 *
 * @author Shengzhao Li
 */
public class UserRolesRowMapper implements RowMapper<UserRoles> {


    @Override
    public UserRoles mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRoles roles = new UserRoles();

        roles.createTime(rs.getTimestamp("create_time"));
        roles.usersId(rs.getInt("users_id"));
        roles.rolesId(rs.getInt("roles_id"));

        return roles;
    }
}
