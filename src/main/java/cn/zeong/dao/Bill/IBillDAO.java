package cn.zeong.dao.Bill;

import cn.zeong.pojo.Bill;
import cn.zeong.pojo.Provider;
import cn.zeong.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IBillDAO {
    //获取订单列表
    public List<Bill> getBillList(Connection connection,String queryProductName,int queryProviderId,int queryIsPayment,int currentPageNo,int pageSize) throws SQLException;
    //获取供应商列表
    public List<Provider> getProviderList(Connection connection) throws SQLException;
    //通过id获取订单信息
    public Bill getBillById(Connection connection,int id) throws SQLException;
    //通过id修改订单
    public boolean updateBillById(Connection connection,Bill bill) throws SQLException;
    //获取订单总数量
    public int getTotalCount(Connection connection,String queryProductName,int queryProviderId,int queryIsPayment) throws SQLException;
    //通过id删除订单
    public boolean delBill(Connection connection,int billId) throws SQLException;
    //添加订单
    public boolean add(Connection connection,Bill bill) throws SQLException;
    //通过商品编码查询订单是否存在
    public boolean getBillByBillCode(Connection connection,String billCode) throws SQLException;
}
