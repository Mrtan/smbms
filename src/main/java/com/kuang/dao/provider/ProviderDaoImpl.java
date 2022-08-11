package com.kuang.dao.provider;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Provider;
import com.kuang.utils.Constants;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.SqlDataResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao {
    public List<Provider> getProviders() throws Exception {
        String sql = "select * from smbms_provider";
        Object[] params = {};
        List<Provider> providers = this.searchProviders(sql, params);
        return providers;
    }

    public List<Provider> getProviders(String proCode, String proName, int pageIndex) throws Exception {
        String sql = "select * from smbms_provider where true";
        List params = new ArrayList();
        if (!StringUtils.isNullOrEmpty(proCode)) {
            sql += " and proCode like ?";
            params.add("%" + proCode + "%");
        }
        if (!StringUtils.isNullOrEmpty(proName)) {
            sql += " and proName like ?";
            params.add("%" + proName + "%");
        }
        if (pageIndex > 0) {
            sql += " limit ?, ?";
            params.add(Constants.PAGE_SIZE * (pageIndex - 1));
            params.add(Constants.PAGE_SIZE);
        }

        List<Provider> providers = this.searchProviders(sql, params.toArray());
        return providers;
    }

    public int getBillsCount(String proCode, String proName, int pageIndex) throws Exception {
        String sql = "select count(id) as count from smbms_provider where true";
        List params = new ArrayList();
        if (!StringUtils.isNullOrEmpty(proCode)) {
            sql += " and proCode like ?";
            params.add("%" + proCode + "%");
        }
        if (!StringUtils.isNullOrEmpty(proName)) {
            sql += " and proName like ?";
            params.add("%" + proName + "%");
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

    public Provider getProviderById(String id) throws Exception {
        String sql = "select * from smbms_provider where id = ?";
        Object[] params = { id };
        List<Provider> providers = this.searchProviders(sql, params);
        return providers.get(0);
    }

    public int updateProvider(Provider provider) throws Exception {
        String sql = "update smbms_provider set proCode = ?, proName = ?, proDesc = ?, proContact = ?, proPhone = ?, proAddress = ?, proFax = ?, " +
                "modifyBy = ?, modifyDate = ? where id = ?";
        Object[] params = { provider.getProCode(), provider.getProName(), provider.getProDesc(), provider.getProContact(), provider.getProPhone(),
                provider.getProAddress(), provider.getProFax(), provider.getModifyBy(), provider.getModifyDate(), provider.getId() };
        return this.updateProvider(sql, params);
    }

    public int addProvider(Provider provider) throws Exception {
        String sql = "insert into smbms_provider set proCode = ?, proName = ?, proDesc = ?, proContact = ?, proPhone = ?, proAddress = ?, proFax = ?, createdBy = ?, creationDate = ?";
        Object[] params = { provider.getProCode(), provider.getProName(), provider.getProDesc(), provider.getProContact(), provider.getProPhone(),
                provider.getProAddress(), provider.getProFax(), provider.getCreatedBy(), provider.getCreationDate() };
        return this.updateProvider(sql, params);
    }

    public int deleteProvider(Provider provider) throws Exception {
        String sql = "delete from smbms_provider where id = ?";
        Object[] params = { provider.getId() };
        return this.updateProvider(sql, params);
    }

    private List<Provider> searchProviders(String sql, Object[] params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Provider> providers = new ArrayList<Provider>();
        resultSet = BaseDao.excute(connection, preparedStatement, resultSet, sql, params);
        while (resultSet.next()) {
            Provider provider = new Provider();
            provider.setId(resultSet.getInt("id"));
            provider.setProCode(resultSet.getString("proCode"));
            provider.setProName(resultSet.getString("proName"));
            provider.setProDesc(resultSet.getString("proDesc"));
            provider.setProContact(resultSet.getString("proContact"));
            provider.setProPhone(resultSet.getString("proPhone"));
            provider.setProAddress(resultSet.getString("proAddress"));
            provider.setProFax(resultSet.getString("proFax"));
            provider.setCreatedBy(resultSet.getInt("createdBy"));
            provider.setCreationDate(resultSet.getDate("creationDate"));
            provider.setModifyBy(resultSet.getInt("modifyBy"));
            provider.setModifyDate(resultSet.getDate("modifyDate"));
            providers.add(provider);
        }

        BaseDao.closeResource(connection, preparedStatement, resultSet);
        return providers;
    }

    private int updateProvider(String sql, Object[] params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int updateNum = BaseDao.excute(connection, preparedStatement, sql, params);

        BaseDao.closeResource(connection, preparedStatement, null);
        return updateNum;
    }
}
