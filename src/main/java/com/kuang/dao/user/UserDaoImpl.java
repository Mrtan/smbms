package com.kuang.dao.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;
import com.kuang.utils.Constants;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{

    public List<User> getUsers(String queryname, String queryUserRole, int pageIndex) throws Exception {
        String sql = "select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id";
        List params = new ArrayList();
        if (!StringUtils.isNullOrEmpty(queryname)) {
            sql += " and u.userName like ?";
            params.add("%" + queryname + "%");
        }
        if (!StringUtils.isNullOrEmpty(queryUserRole) && !"0".equals(queryUserRole)) {
            sql += " and u.userRole = ?";
            params.add(queryUserRole);
        }
        if (pageIndex > 0) {
            sql += " limit ?, ?";
            params.add(Constants.PAGE_SIZE * (pageIndex - 1));
            params.add(Constants.PAGE_SIZE);
        }

        List<User> users = this.SearchUsers(sql, params.toArray());
        return users;
    }

    public int getUsersCount(String queryname, String queryUserRole) throws Exception {
        String sql = "select count(u.id) as count from smbms_user u, smbms_role r where u.userRole = r.id";
        List params = new ArrayList();
        if (!StringUtils.isNullOrEmpty(queryname)) {
            sql += " and u.userName like ?";
            params.add("%" + queryname + "%");
        }
        if (!StringUtils.isNullOrEmpty(queryUserRole) && !"0".equals(queryUserRole)) {
            sql += " and u.userRole = ?";
            params.add(queryUserRole);
        }

        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        resultSet = BaseDao.excute(connection, preparedStatement, resultSet, sql, params.toArray());
        if (resultSet.first()) count = resultSet.getInt("count");

        BaseDao.closeResource(connection, preparedStatement, resultSet);
        return count;
    }

    public User getUserByUserCode(String userCode) throws Exception {
        String sql = "select *, '' as userRoleName from smbms_user where userCode = ?";
        Object[] params = {userCode};
        List<User> users = this.SearchUsers(sql, params);
        return users.size() > 0 ? users.get(0) : null;
    }

    public User getUserById(String id) throws Exception {
        String sql = "select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id and u.id = ?";
        Object[] params = {id};
        List<User> users = this.SearchUsers(sql, params);
        return users.size() > 0 ? users.get(0) : null;
    }

    public int modifyUser(User user) throws Exception {
        String sql = "update smbms_user set userName = ?, userPassword = ?, gender = ?, birthday = ?, phone = ?, address = ?, userRole = ?, modifyBy = ?, modifyDate = ? where id = ?";
        Object[] params = {user.getUserName(), user.getUserPassword(), user.getGender(), user.getBirthday(), user.getPhone(), user.getAddress(),
                user.getUserRole(), user.getModifyBy(), user.getModifyDate(), user.getId()};
        return this.updateUser(sql, params);
    }

    public int addUser(User user) throws Exception {
        String sql = "insert into smbms_user (userCode, userName, userPassword, gender, birthday, phone, address, userRole, createdBy, creationDate) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(), user.getGender(), user.getBirthday(),
                user.getPhone(), user.getAddress(), user.getUserRole(), user.getCreatedBy(), user.getCreationDate()};
        return this.updateUser(sql, params);
    }

    public int deleteUser(User user) throws Exception {
        String sql = "delete from smbms_user where id = ?";
        Object[] params = { user.getId() };
        int deletedNum = this.updateUser(sql, params);
        return deletedNum;
    }

    private List<User> SearchUsers(String sql, Object[] params) throws Exception {
        List<User> users = new ArrayList<User>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        resultSet = BaseDao.excute(connection, preparedStatement, resultSet, sql, params);
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUserCode(resultSet.getString("userCode"));
            user.setUserName(resultSet.getString("userName"));
            user.setUserPassword(resultSet.getString("userPassword"));
            user.setGender(resultSet.getInt("gender"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setPhone(resultSet.getString("phone"));
            user.setAddress(resultSet.getString("address"));
            user.setUserRole(resultSet.getInt("userRole"));
            user.setCreatedBy(resultSet.getInt("createdBy"));
            user.setModifyBy(resultSet.getInt("modifyBy"));
            user.setModifyDate(resultSet.getDate("modifyDate"));
            user.setUserRoleName(resultSet.getString("userRoleName"));

            users.add(user);
        }

        BaseDao.closeResource(connection, preparedStatement, resultSet);
        return users;
    }

    private int updateUser(String sql, Object[] params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int updatedNum = BaseDao.excute(connection, preparedStatement, sql, params);

        BaseDao.closeResource(connection, preparedStatement, null);
        return updatedNum;
    }
}
