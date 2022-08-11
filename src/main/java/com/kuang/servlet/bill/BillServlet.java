package com.kuang.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuang.pojo.Bill;
import com.kuang.pojo.Provider;
import com.kuang.pojo.User;
import com.kuang.service.bill.BillService;
import com.kuang.service.bill.BillServiceImpl;
import com.kuang.service.provider.ProviderService;
import com.kuang.service.provider.ProviderServiceImpl;
import com.kuang.utils.Constants;
import com.kuang.utils.PageSupport;
import com.mysql.cj.util.StringUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BillService billService = new BillServiceImpl();
        ProviderService providerService = new ProviderServiceImpl();
        User curUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);

        String method = req.getParameter("method");
        String id = req.getParameter("id");
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");
        String pageIndex = req.getParameter("pageIndex");

        int isPaymentInt = (StringUtils.isNullOrEmpty(isPayment)) ? 0 : Integer.parseInt(isPayment);
        int providerIdInt = (StringUtils.isNullOrEmpty(providerId)) ? 0 : Integer.parseInt(providerId);
        if ("query".equals(method)) {
            int currentPageNo = (StringUtils.isNullOrEmpty(pageIndex)) ? 1 : Integer.parseInt(pageIndex);
            int totalCount = billService.getBillsCount(productName, providerIdInt, isPaymentInt);
            PageSupport pageSupport = new PageSupport(totalCount, currentPageNo);
            req.setAttribute("totalCount", pageSupport.getTotalCount());
            req.setAttribute("totalPageCount", pageSupport.getTotalPageCount());
            req.setAttribute("currentPageNo", pageSupport.getCurrentPageNo());

            List<Bill> bills = billService.getBills(productName, providerIdInt, isPaymentInt, currentPageNo);
            req.setAttribute("billList", bills);

            List<Provider> providers = providerService.getProviders();
            req.setAttribute("providerList", providers);

            req.setAttribute("productName", productName);
            req.setAttribute("providerId", providerIdInt);
            req.setAttribute("isPayment", isPaymentInt);
            req.getRequestDispatcher("billlist.jsp").forward(req, resp);
        }

        if ("view".equals(method)) {
            Bill bill = billService.getBillById(id);
            req.setAttribute("bill", bill);
            req.getRequestDispatcher("billview.jsp").forward(req, resp);
        }

        if ("modify".equals(method)) {
            Bill bill = billService.getBillById(id);
            req.setAttribute("bill", bill);
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }

        if ("modifysave".equals(method)) {
            Bill bill = this.getBillByParams(curUser, id, billCode, productName, productUnit, productCount, totalPrice, providerIdInt, isPaymentInt);
            Boolean isModified = billService.modifyBill(bill);
            Bill newbill = billService.getBillById(id);
            req.setAttribute("bill", newbill);
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }

        if ("add".equals(method)) {
            Bill bill = this.getBillByParams(curUser, id, billCode, productName, productUnit, productCount, totalPrice, providerIdInt, isPaymentInt);
            Boolean isAdded = billService.addBill(bill);
            req.getRequestDispatcher("billadd.jsp").forward(req, resp);
        }

        if ("delbill".equals(method)) {
            resp.setContentType("application/json");

            String delResult = "notexist";
            Bill bill = billService.getBillById(id);
            if (bill != null) {
                Boolean isDeleted = billService.deleteBill(bill);
                delResult = isDeleted ? "true" : "false";
            }
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("delResult", delResult);
            PrintWriter writer = resp.getWriter();
            writer.write(JSONObject.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }

        if ("getproviderlist".equals(method)) {
            resp.setContentType("application/json");

            List<Provider> providers = providerService.getProviders();
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(providers));
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private Bill getBillByParams(User curUser, String id, String billCode, String productName, String productUnit, String productCount, String totalPrice, int providerIdInt, int isPaymentInt) {
        Date date = new Date();
        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProviderId(providerIdInt);
        bill.setIsPayment(isPaymentInt);
        bill.setCreatedBy(curUser.getId());
        bill.setCreationDate(date);
        bill.setModifyBy(curUser.getId());
        bill.setModifyDate(date);
        if (id != null) bill.setId(Integer.parseInt(id));
        if (productCount != null) bill.setProductCount(Integer.parseInt(productCount));
        if (totalPrice != null) bill.setTotalPrice(Double.parseDouble(totalPrice));
        return bill;
    }
}
