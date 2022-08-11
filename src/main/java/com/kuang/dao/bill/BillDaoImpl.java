package com.kuang.dao.bill;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Bill;
import com.kuang.utils.Constants;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements BillDao {
    public List<Bill> getBills(String productName, int providerId, int isPayment, int pageIndex) throws Exception {
        String sql = "select b.*, p.proName as providerName from smbms_bill b, smbms_provider p where b.providerId = p.id";
        List params = new ArrayList();
        if (!StringUtils.isNullOrEmpty(productName)) {
            sql += " and b.productName like ?";
            params.add("%" + productName + "");
        }
        if (providerId > 0) {
            sql += " and b.providerId = ?";
            params.add(providerId);
        }
        if (isPayment > 0) {
            sql += " and b.isPayment = ?";
            params.add(isPayment + "");
        }
        if (pageIndex > 0) {
            sql += " limit ?, ?";
            params.add(Constants.PAGE_SIZE * (pageIndex - 1));
            params.add(Constants.PAGE_SIZE);
        }

        List<Bill> bills = this.searchBills(sql, params.toArray());
        return bills;
    }

    public int getBillsCount(String productName, int providerId, int isPayment) throws Exception {
        String sql = "select count(b.id) as count from smbms_bill b, smbms_provider p where b.providerId = p.id";
        List params = new ArrayList();
        if (!StringUtils.isNullOrEmpty(productName)) {
            sql += " and b.productName like ?";
            params.add("%" + productName + "");
        }
        if (providerId > 0) {
            sql += " and b.providerId = ?";
            params.add(providerId);
        }
        if (isPayment > 0) {
            sql += " and b.isPayment = ?";
            params.add(isPayment + "");
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

    public Bill getBillById(String id) throws Exception {
        String sql = "select b.*, p.proName as providerName from smbms_bill b, smbms_provider p where b.providerId = p.id and b.id = ?";
        Object[] params = { id };
        List<Bill> bills = this.searchBills(sql, params);
        return bills.size() > 0 ? bills.get(0) : null;
    }

    public int modifyBill(Bill bill) throws Exception {
        String sql = "update smbms_bill set billCode = ?, productName = ?, productUnit = ?, productCount = ?, " +
                "totalPrice = ?, providerId = ?, isPayment = ?, modifyBy = ?, modifyDate = ? where id = ?";
        Object[] params = { bill.getBillCode(), bill.getProductName(), bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(),
                bill.getProviderId(), bill.getIsPayment(), bill.getModifyBy(), bill.getModifyDate(), bill.getId() };
        return this.updateBill(sql, params);
    }

    public int addBill(Bill bill) throws Exception {
        String sql = "insert into smbms_bill set billCode = ?, productName = ?, productUnit = ?, productCount = ?, " +
                "totalPrice = ?, providerId = ?, isPayment = ?, createdBy = ?, creationDate = ?";
        Object[] params = { bill.getBillCode(), bill.getProductName(), bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(),
                bill.getProviderId(), bill.getIsPayment(), bill.getCreatedBy(), bill.getCreationDate() };
        return this.updateBill(sql, params);
    }

    public int deleteBill(Bill bill) throws Exception {
        String sql = "delete from smbms_bill where id = ?";
        Object[] params = { bill.getId() };
        return this.updateBill(sql, params);
    }

    private List<Bill> searchBills(String sql, Object[] params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Bill> bills = new ArrayList<Bill>();
        resultSet = BaseDao.excute(connection, preparedStatement, resultSet, sql, params);
        while (resultSet.next()) {
            Bill bill = new Bill();
            bill.setId(resultSet.getInt("id"));
            bill.setBillCode(resultSet.getString("billCode"));
            bill.setProductName(resultSet.getString("productName"));
            bill.setProductDesc(resultSet.getString("productDesc"));
            bill.setProductUnit(resultSet.getString("productUnit"));
            bill.setProductCount(resultSet.getInt("productCount"));
            bill.setTotalPrice(resultSet.getDouble("totalPrice"));
            bill.setIsPayment(resultSet.getInt("isPayment"));
            bill.setCreatedBy(resultSet.getInt("createdBy"));
            bill.setCreationDate(resultSet.getDate("creationDate"));
            bill.setModifyBy(resultSet.getInt("modifyBy"));
            bill.setModifyDate(resultSet.getDate("modifyDate"));
            bill.setProviderId(resultSet.getInt("providerId"));
            bill.setProviderName(resultSet.getString("providerName"));
            bills.add(bill);
        }

        BaseDao.closeResource(connection, preparedStatement, resultSet);
        return bills;
    }

    private int updateBill(String sql, Object[] params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int updatedNum = BaseDao.excute(connection, preparedStatement, sql, params);

        BaseDao.closeResource(connection, preparedStatement, null);
        return updatedNum;
    }
}
