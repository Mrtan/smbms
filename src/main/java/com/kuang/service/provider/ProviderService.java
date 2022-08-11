package com.kuang.service.provider;

import com.kuang.pojo.Provider;

import java.util.List;

public interface ProviderService {
    public List<Provider> getProviders();
    public List<Provider> getProviders(String proCode, String proName, int pageIndex);
    public int getProvidersCount(String proCode, String proName, int pageIndex);
    public Provider getProviderById(String id);
    public Boolean updateProvider(Provider provider);
    public Boolean addProvider(Provider provider);
    Boolean deleteProvider(Provider provider);
}
