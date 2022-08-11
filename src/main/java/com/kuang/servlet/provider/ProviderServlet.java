package com.kuang.servlet.provider;

import com.alibaba.fastjson.JSONObject;
import com.kuang.pojo.Provider;
import com.kuang.pojo.User;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProviderService providerService = new ProviderServiceImpl();
        User curUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);

        String method = req.getParameter("method");
        String id = req.getParameter("id");
        String pageIndex = req.getParameter("pageIndex");
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proFax = req.getParameter("proFax");
        String proAddress = req.getParameter("proAddress");
        String proDesc = req.getParameter("proDesc");

        if ("query".equals(method)) {
            int currentPageNo = (StringUtils.isNullOrEmpty(pageIndex)) ? 1 : Integer.parseInt(pageIndex);
            int totalCount = providerService.getProvidersCount(proCode, proName, currentPageNo);
            PageSupport pageSupport = new PageSupport(totalCount, currentPageNo);
            req.setAttribute("totalCount", pageSupport.getTotalCount());
            req.setAttribute("totalPageCount", pageSupport.getTotalPageCount());
            req.setAttribute("currentPageNo", pageSupport.getCurrentPageNo());

            List<Provider> providers = providerService.getProviders(proCode, proName, currentPageNo);
            req.setAttribute("providerList", providers);

            req.setAttribute("proCode", proCode);
            req.setAttribute("proName", proName);
            req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
        }

        if ("view".equals(method)) {
            Provider provider = providerService.getProviderById(id);
            req.setAttribute("provider", provider);
            req.getRequestDispatcher("providerview.jsp").forward(req, resp);
        }

        if ("modify".equals(method)) {
            Provider provider = providerService.getProviderById(id);
            req.setAttribute("provider", provider);
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }

        if ("modifysave".equals(method)) {
            Provider provider = this.getProviderByParams(curUser, id, proCode, proName, proContact, proPhone, proAddress, proFax, proDesc);
            Boolean isModified = providerService.updateProvider(provider);
            Provider newprovider = providerService.getProviderById(id);
            req.setAttribute("provider", newprovider);
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }

        if ("add".equals(method)) {
            Provider provider = this.getProviderByParams(curUser, id, proCode, proName, proContact, proPhone, proAddress, proFax, proDesc);
            Boolean isAdded = providerService.addProvider(provider);
            req.getRequestDispatcher("provideradd.jsp").forward(req, resp);
        }

        if ("delprovider".equals(method)) {
            resp.setContentType("application/json");

            String delResult = "notexist";
            Provider provider = providerService.getProviderById(id);
            if (provider != null) {
                Boolean isDeleted = providerService.deleteProvider(provider);
                delResult = isDeleted ? "true" : "false";
            }
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("delResult", delResult);
            PrintWriter writer = resp.getWriter();
            writer.write(JSONObject.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private Provider getProviderByParams(User curUser, String id, String proCode, String proName, String proContact, String proPhone, String proAddress, String proFax, String proDesc) {
        Provider provider = new Provider();
        Date date = new Date();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(curUser.getId());
        provider.setCreationDate(date);
        provider.setModifyBy(curUser.getId());
        provider.setModifyDate(date);
        if (id != null) provider.setId(Integer.parseInt(id));
        return provider;
    }
}
