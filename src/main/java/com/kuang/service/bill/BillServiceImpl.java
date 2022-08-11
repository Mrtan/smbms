package com.kuang.service.bill;

import com.kuang.dao.bill.BillDao;
import com.kuang.dao.bill.BillDaoImpl;
import com.kuang.pojo.Bill;

import java.util.List;

public class BillServiceImpl implements BillService {
    public List<Bill> getBills(String productName, int providerId, int isPayment, int pageIndex) {
        List<Bill> bills = null;
        BillDao billDao = new BillDaoImpl();
        try {
            bills = billDao.getBills(productName, providerId, isPayment, pageIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bills;
    }

    public int getBillsCount(String productName, int providerId, int isPayment) {
        int count = 0;
        BillDao billDao = new BillDaoImpl();
        try {
            count = billDao.getBillsCount(productName, providerId, isPayment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public Bill getBillById(String id) {
        Bill bill = null;
        BillDao billDao = new BillDaoImpl();
        try {
            bill = billDao.getBillById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bill;
    }

    public boolean modifyBill(Bill bill) {
        int updatedNum = 0;
        BillDao billDao = new BillDaoImpl();
        try {
            updatedNum = billDao.modifyBill(bill);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return updatedNum > 0;
    }

    public boolean addBill(Bill bill) {
        int updatedNum = 0;
        BillDao billDao = new BillDaoImpl();
        try {
            updatedNum = billDao.addBill(bill);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return updatedNum > 0;
    }

    public boolean deleteBill(Bill bill) {
        int updatedNum = 0;
        BillDao billDao = new BillDaoImpl();
        try {
            updatedNum = billDao.deleteBill(bill);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return updatedNum > 0;
    }
}
