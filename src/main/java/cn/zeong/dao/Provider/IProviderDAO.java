package cn.zeong.dao.Provider;

import cn.zeong.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IProviderDAO {
    //获取供应商列表
    public List<Provider> getProviderList(Connection connection,String queryProCode,String queryProName,int currentPageNo,int pageSize) throws SQLException;
    //获取供应商总数量
    public int totalCount(Connection connection,String queryProCode,String queryProName) throws SQLException;
    //通过id获取订单信息
    public Provider getProviderById(Connection connection,int id) throws SQLException;

    //修改供应商编码时是否存在除本身的其他相同的编码
    public boolean pcExist(Connection connection,String proCode) throws SQLException;

    //通过id修改供应商信息
    public boolean updateProvider(Connection connection,Provider provider) throws SQLException;
    //通过id删除供应商
    public boolean delProvider(Connection connection,int id) throws SQLException;
    //添加供应商
    public boolean addProvider(Connection connection,Provider provider) throws SQLException;
}
