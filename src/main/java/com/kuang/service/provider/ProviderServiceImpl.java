package com.kuang.service.provider;

import com.kuang.dao.provider.ProviderDao;
import com.kuang.dao.provider.ProviderDaoImpl;
import com.kuang.pojo.Provider;

import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    public List<Provider> getProviders() {
        List<Provider> providers = null;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            providers = providerDao.getProviders();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return providers;
    }

    public List<Provider> getProviders(String proCode, String proName, int pageIndex) {
        List<Provider> providers = null;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            providers = providerDao.getProviders(proCode, proName, pageIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return providers;
    }

    public int getProvidersCount(String proCode, String proName, int pageIndex) {
        int count = 0;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            count = providerDao.getBillsCount(proCode, proName, pageIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public Provider getProviderById(String id) {
        Provider provider = null;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            provider = providerDao.getProviderById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return provider;
    }

    public Boolean updateProvider(Provider provider) {
        int updateNum = 0;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            updateNum = providerDao.updateProvider(provider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return updateNum > 0;
    }

    public Boolean addProvider(Provider provider) {
        int updateNum = 0;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            updateNum = providerDao.addProvider(provider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return updateNum > 0;
    }

    public Boolean deleteProvider(Provider provider) {
        int updateNum = 0;
        ProviderDao providerDao = new ProviderDaoImpl();
        try {
            updateNum = providerDao.deleteProvider(provider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return updateNum > 0;
    }
}
