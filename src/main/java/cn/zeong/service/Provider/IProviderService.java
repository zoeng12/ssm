package cn.zeong.service.Provider;

import cn.zeong.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface IProviderService {
    public List<Provider> getProviderList(String queryProCode,String queryProName,int currentPageNo,int pageSize) throws SQLException;
    public int totalCount(String queryProCode,String queryProName) throws SQLException;
    public Provider getProviderById(int id) throws SQLException;
    public boolean pcExist(String proCode) throws SQLException;
    public boolean updateProvider(Provider provider) throws SQLException;
    public boolean delProvider(int id) throws SQLException;
    public boolean addProvider(Provider provider) throws SQLException;
}
