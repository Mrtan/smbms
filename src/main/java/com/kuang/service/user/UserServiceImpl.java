package com.kuang.service.user;

import com.kuang.dao.user.UserDao;
import com.kuang.dao.user.UserDaoImpl;
import com.kuang.pojo.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    public User login(String userCode, String userPassword) {
        User user = null;
        UserDao userDao = new UserDaoImpl();
        try {
            user = userDao.getUserByUserCode(userCode);
            if (user != null && !userPassword.equals(user.getUserPassword())) user = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getUsers(String queryname, String queryUserRole, int pageIndex) {
        List<User> users = null;
        UserDao userDao = new UserDaoImpl();
        try {
            users = userDao.getUsers(queryname, queryUserRole, pageIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public int getUsersCount(String queryname, String queryUserRole) {
        int count = 0;
        UserDao userDao = new UserDaoImpl();
        try {
            count = userDao.getUsersCount(queryname, queryUserRole);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public Boolean getIsUserExistByUserCode(String userCode) {
        Boolean isExist = false;
        UserDao userDao = new UserDaoImpl();
        try {
            User user = userDao.getUserByUserCode(userCode);
            if (user != null) isExist = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public User getUserById(String id) {
        User user = null;
        UserDao userDao = new UserDaoImpl();
        try {
            user = userDao.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public Boolean modifyUser(User user) {
        int updatedNum = 0;
        UserDao userDao = new UserDaoImpl();
        try {
            updatedNum = userDao.modifyUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedNum > 0;
    }

    public Boolean addUser(User user) {
        int updatedNum = 0;
        UserDao userDao = new UserDaoImpl();
        try {
            updatedNum = userDao.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedNum > 0;
    }

    public Boolean deleteUser(User user) {
        int deletedNum = 0;
        UserDao userDao = new UserDaoImpl();
        try {
            deletedNum = userDao.deleteUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deletedNum > 0;
    }
}
