package cn.zeong.service.Bill;

import cn.zeong.pojo.Bill;
import cn.zeong.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IBillService {
    public List<Bill> getBillList(String queryProductName,int queryProviderId,int queryIsPayment,int currentPageNo,int pageSize) throws SQLException;
    public List<Provider> getProviderList() throws SQLException;
    public Bill getBillById(int id) throws SQLException;
    public boolean updateBillById(Bill bill) throws SQLException;
    public int getTotalCount(String queryProductName, int queryProviderId, int queryIsPayment) throws SQLException;
    public boolean delBill(int billId) throws SQLException;
    public boolean add(Bill bill) throws SQLException;
    public boolean getBillByBillCode(String billCode) throws SQLException;
}
