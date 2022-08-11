package com.kuang.service.user;

import com.kuang.pojo.User;

import java.util.List;

public interface UserService {
    public User login(String userCode, String userPassword);

    public List<User> getUsers(String queryname, String queryUserRole, int pageIndex);

    public int getUsersCount(String queryname, String queryUserRole);

    public Boolean getIsUserExistByUserCode(String userCode);

    public User getUserById(String id);

    public Boolean modifyUser(User user);

    public Boolean addUser(User user);

    public Boolean deleteUser(User user);
}
