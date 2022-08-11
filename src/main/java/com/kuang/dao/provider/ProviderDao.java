package com.kuang.dao.provider;

import com.kuang.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    public List<Provider> getProviders() throws Exception;
    public List<Provider> getProviders(String proCode, String proName, int pageIndex) throws Exception;
    public int getBillsCount(String proCode, String proName, int pageIndex) throws Exception;
    public Provider getProviderById(String id) throws Exception;
    public int updateProvider(Provider provider) throws Exception;
    public int addProvider(Provider provider) throws Exception;
    int deleteProvider(Provider provider) throws Exception;
}
