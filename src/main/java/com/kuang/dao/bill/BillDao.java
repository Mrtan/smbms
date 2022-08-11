package com.kuang.dao.bill;

import com.kuang.pojo.Bill;

import java.util.List;

public interface BillDao {
    public List<Bill> getBills(String productName, int providerId, int isPayment, int pageIndex) throws Exception;

    public int getBillsCount(String productName, int providerId, int isPayment) throws Exception;

    public Bill getBillById(String id) throws Exception;

    public int modifyBill(Bill bill) throws Exception;

    public int addBill(Bill bill) throws Exception;

    public int deleteBill(Bill bill) throws Exception;
}
