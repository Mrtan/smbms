package com.kuang.dao.role;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Role;
import com.sun.javaws.jnl.RContentDesc;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    public List<Role> getRoles() throws Exception {
        List<Role> roles = new ArrayList<Role>();
        String sql = "select * from smbms_role";
        Connection connection = null;;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {};

        resultSet = BaseDao.excute(connection, preparedStatement, resultSet, sql, params);
        while (resultSet.next()) {
           Role role = new Role();
           role.setId(resultSet.getInt("id"));
           role.setRoleCode(resultSet.getString("roleCode"));
           role.setRoleName(resultSet.getString("roleName"));
           role.setCreatedBy(resultSet.getInt("createdBy"));
           role.setCreationDate(resultSet.getDate("creationDate"));
           role.setModifyBy(resultSet.getInt("modifyBy"));
           role.setModifyDate(resultSet.getDate("modifyDate"));
           roles.add(role);
        }

        BaseDao.closeResource(connection, preparedStatement, resultSet);
        return roles;
    }
}
