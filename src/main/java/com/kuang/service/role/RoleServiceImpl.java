package com.kuang.service.role;

import com.kuang.dao.role.RoleDao;
import com.kuang.dao.role.RoleDaoImpl;
import com.kuang.pojo.Role;

import java.util.List;

public class RoleServiceImpl implements RoleService{
    public List<Role> getRoles() {
        List<Role> roles = null;
        RoleDao roleDao = new RoleDaoImpl();
        try {
            roles = roleDao.getRoles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return roles;
    }
}
