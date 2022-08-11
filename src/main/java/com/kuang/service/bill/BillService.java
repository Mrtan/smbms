package com.kuang.service.bill;

import com.kuang.pojo.Bill;

import java.util.List;

public interface BillService {
    public List<Bill> getBills(String productName, int providerId, int isPayment, int pageIndex);

    public int getBillsCount(String productName, int providerId, int isPayment);

    public Bill getBillById(String id);

    public boolean modifyBill(Bill bill);

    public boolean addBill(Bill bill);

    public boolean deleteBill(Bill bill);
}
