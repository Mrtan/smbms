package com.kuang.dao.user;

import com.kuang.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public List<User> getUsers(String queryname, String queryUserRole, int pageIndex) throws Exception;

    public int getUsersCount(String queryname, String queryUserRole) throws Exception;

    public User getUserByUserCode(String userCode) throws Exception;

    public User getUserById(String id) throws Exception;

    public int modifyUser(User user) throws Exception;

    public int addUser(User user) throws Exception;

    public int deleteUser(User user) throws Exception;
}
