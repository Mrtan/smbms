package com.kuang.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuang.pojo.Role;
import com.kuang.pojo.User;
import com.kuang.service.role.RoleService;
import com.kuang.service.role.RoleServiceImpl;
import com.kuang.service.user.UserService;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.utils.Constants;
import com.kuang.utils.PageSupport;
import com.mysql.cj.util.StringUtils;
import com.oracle.wls.shaded.org.apache.bcel.classfile.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User curUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        UserService userService = new UserServiceImpl();
        RoleService roleService = new RoleServiceImpl();

        String method = req.getParameter("method");
        String queryname = req.getParameter("queryname");
        String queryUserRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        String userCode = req.getParameter("userCode");
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        String birthday = req.getParameter("birthday");
        String ruserPassword = req.getParameter("ruserPassword");
        String oldpassword = req.getParameter("oldpassword");
        String rnewpassword = req.getParameter("rnewpassword");

        if ("query".equals(method)) {
            int totalCount = userService.getUsersCount(queryname, queryUserRole);
            int currentPageNo = (StringUtils.isNullOrEmpty(pageIndex)) ? 1 : Integer.parseInt(pageIndex);
            PageSupport pageSupport = new PageSupport(totalCount, currentPageNo);
            req.setAttribute("totalCount", pageSupport.getTotalCount());
            req.setAttribute("totalPageCount", pageSupport.getTotalPageCount());
            req.setAttribute("currentPageNo", pageSupport.getCurrentPageNo());
            req.setAttribute("queryUserName", queryname);
            req.setAttribute("queryUserRole", queryUserRole);

            List<User> users = userService.getUsers(queryname, queryUserRole, currentPageNo);
            req.setAttribute("userList", users);

            List<Role> roles = roleService.getRoles();
            req.setAttribute("roleList", roles);

            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        }

        if ("view".equals(method)) {
            User user = userService.getUserById(uid);
            req.setAttribute("user", user);
            req.getRequestDispatcher("userview.jsp").forward(req, resp);
        }

        if ("modify".equals(method)) {
            User user = userService.getUserById(uid);
            req.setAttribute("user", user);
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }

        if ("modifyexe".equals(method)) {
            User user = this.getUserByParams(curUser, uid, userCode, ruserPassword, userName, gender, phone, address, userRole, birthday);
            Boolean isModified = userService.modifyUser(user);
            User newuser = userService.getUserById(uid);
            req.setAttribute("user", newuser);
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }

        if ("add".equals(method)) {
            User user = this.getUserByParams(curUser, uid, userCode, ruserPassword, userName, gender, phone, address, userRole, birthday);
            Boolean isAdded = userService.addUser(user);
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }

        if ("deluser".equals(method)) {
            resp.setContentType("application/json");

            String delResult = "notexist";
            User user = userService.getUserById(uid);
            if (user != null) {
                Boolean isDeleted = userService.deleteUser(user);
                delResult = isDeleted ? "true" : "false";
            }
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("delResult", delResult);
            PrintWriter writer = resp.getWriter();
            writer.write(JSONObject.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }

        if ("ucexist".equals(method)) {
            resp.setContentType("application/json");

            Map<String, String> resultMap = new HashMap<String, String>();
            boolean isExist = userService.getIsUserExistByUserCode(userCode);
            if (isExist) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "usable");
            }
            PrintWriter writer = resp.getWriter();
            writer.write(JSONObject.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }

        if ("getrolelist".equals(method)) {
            resp.setContentType("application/json");

            List<Role> roles = roleService.getRoles();
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(roles));
            writer.flush();
            writer.close();
        }

        if ("pwdmodify".equals(method)) {
            resp.setContentType("application/json");
            String result = "sessionerror";
            if (curUser != null) result = (curUser.getUserPassword().equals(oldpassword)) ? "true" : "false";
            Map data = new HashMap();
            data.put("result", result);
            PrintWriter writer = resp.getWriter();
            writer.write(JSONObject.toJSONString(data));
            writer.flush();
            writer.close();
        }

        if ("savepwd".equals(method)) {
            User user = this.getUserByParams(curUser, curUser.getId() + "", curUser.getUserCode(), rnewpassword, curUser.getUserName(),
                    curUser.getGender() + "", curUser.getPhone(), curUser.getAddress(), curUser.getUserRole() + "", curUser.getBirthday() + "");
            Boolean isModified = userService.modifyUser(user);
            if (isModified) resp.sendRedirect("/jsp/logout.do");
            else req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private User getUserByParams(User curUser, String uid, String userCode, String userPassword, String userName, String gender, String phone, String address, String userRole, String birthday) {
        Date date = new Date();
        User user = new User();
        user.setUserCode(userCode);
        user.setUserPassword(userPassword);
        user.setUserName(userName);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCreatedBy(curUser.getId());
        user.setCreationDate(date);
        user.setModifyBy(curUser.getId());
        user.setModifyDate(date);
        if (gender != null) user.setGender(Integer.parseInt(gender));
        if (userRole != null) user.setUserRole(Integer.parseInt(userRole));
        if (uid != null) user.setId(Integer.parseInt(uid));
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            user.setBirthday(simpleDateFormat.parse(birthday));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}
