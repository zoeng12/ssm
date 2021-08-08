package cn.zeong.dao.Bill;

import cn.zeong.dao.BaseDao;
import cn.zeong.pojo.Bill;
import cn.zeong.pojo.Provider;
import cn.zeong.pojo.User;
import com.mysql.jdbc.StringUtils;
import javafx.beans.binding.ObjectExpression;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BillDAOImpl implements IBillDAO{
    @Override
    public List<Bill> getBillList(Connection connection,String queryProductName,int queryProviderId,int queryIsPayment,int currentPageNo,int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Bill> billList = new ArrayList<>();
        //要执行的sql语句
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select * from smbms_bill b,smbms_provider p where b.providerId = p.id");
        if (queryProductName != null && !StringUtils.isNullOrEmpty(queryProductName)) {
            stringBuffer.append(" and b.productName like " + "'%" +queryProductName+ "%'");
        }
        if (queryProviderId !=0) {
            stringBuffer.append(" and p.id = "+queryProviderId);
        }
        if (queryIsPayment !=0){
            stringBuffer.append(" and b.isPayment = "+queryIsPayment);
        }
        stringBuffer.append(" order by b.creationDate desc limit ?,?");
        currentPageNo = (currentPageNo-1)*pageSize;
        Object[] params = {currentPageNo,pageSize};
        String sql = stringBuffer.toString();
        //调用dao层查询方法
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        while (resultSet.next()) {
            Bill bill = new Bill();
            bill.setId(resultSet.getInt("id"));
            bill.setBillCode(resultSet.getString("billCode"));
            bill.setProductName(resultSet.getString("productName"));
            bill.setProductDesc(resultSet.getString("productDesc"));
            bill.setProductUnit(resultSet.getString("productUnit"));
            bill.setProductCount(resultSet.getBigDecimal("productCount"));
            bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
            bill.setIsPayment(resultSet.getInt("isPayment"));
            bill.setCreatedBy(resultSet.getInt("createdBy"));
            bill.setCreationDate(resultSet.getDate("creationDate"));
            bill.setModifyBy(resultSet.getInt("modifyBy"));
            bill.setModifyDate(resultSet.getDate("modifyDate"));
            bill.setProviderId(resultSet.getInt("providerId"));
            bill.setProviderName(resultSet.getString("proName"));
            billList.add(bill);
        }
        //关闭资源
        BaseDao.close(null, preparedStatement, resultSet);
        return billList;
    }

    @Override
    public List<Provider> getProviderList(Connection connection) throws SQLException {
        ArrayList<Provider> providerList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {};
        //将要执行的SQL语句
        String sql = "select * from smbms_provider";
        //执行sql
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        //封装provider
        while (resultSet.next()){
            Provider provider = new Provider();
            provider.setId(resultSet.getInt("id"));
            provider.setProCode(resultSet.getString("proCode"));
            provider.setProName(resultSet.getString("proName"));
            provider.setProDesc(resultSet.getString("proDesc"));
            provider.setProContact(resultSet.getString("proContact"));
            provider.setProPhone(resultSet.getString("proPhone"));
            provider.setProAddress(resultSet.getString("proAddress"));
            provider.setProFax(resultSet.getString("proFax"));
            provider.setCreateBy(resultSet.getInt("createdBy"));
            provider.setCreationDate(resultSet.getDate("creationDate"));
            providerList.add(provider);
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return providerList;
    }

    @Override
    public Bill getBillById(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Bill bill = new Bill();
        //将要执行的SQL语句
        String sql = "select * from smbms_bill b,smbms_provider p where b.providerId = p.id and b.id = ?";
        //设置占位符的值
        Object[] params = {id};
        //执行sql
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        //封装
        while (resultSet.next()){
            bill.setId(resultSet.getInt("id"));
            bill.setBillCode(resultSet.getString("billCode"));
            bill.setProductName(resultSet.getString("productName"));
            bill.setProductDesc(resultSet.getString("productDesc"));
            bill.setProductUnit(resultSet.getString("productUnit"));
            bill.setProductCount(resultSet.getBigDecimal("productCount"));
            bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
            bill.setIsPayment(resultSet.getInt("isPayment"));
            bill.setCreatedBy(resultSet.getInt("createdBy"));
            bill.setCreationDate(resultSet.getDate("creationDate"));
            bill.setModifyBy(resultSet.getInt("modifyBy"));
            bill.setModifyDate(resultSet.getDate("modifyDate"));
            bill.setProviderId(resultSet.getInt("providerId"));
            bill.setProviderName(resultSet.getString("proName"));
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return bill;
    }

    @Override
    public boolean updateBillById(Connection connection,Bill bill) throws SQLException {
        PreparedStatement preparedStatement = null;
        boolean flag = false;
        int execute = 0;
        //将要执行的SQL语句
        String sql = "update smbms_bill set billCode = ?,productName = ?,productUnit = ?,productCount = ?,totalPrice = ?,providerId = ?,isPayment = ?,modifyBy = ?,modifyDate = ? where id = ?";
        //设置占位符的值
        Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getProviderId(),bill.getIsPayment(),bill.getModifyBy(),bill.getModifyDate(),bill.getId()};
        //执行sql
        execute = BaseDao.execute(connection,sql,params,preparedStatement);
        if (execute > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        System.out.println(flag);
        return flag;
    }

    @Override
    public int getTotalCount(Connection connection, String queryProductName, int queryProviderId, int queryIsPayment) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {};
        int totalCount = 0;
        //要执行的sql语句
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select count(1) as totalCount from smbms_bill b,smbms_provider p where b.providerId = p.id");
        if (queryProductName != null && !StringUtils.isNullOrEmpty(queryProductName)) {
            stringBuffer.append(" and b.productName like " + "'%" +queryProductName+ "%'");
        }
        if (queryProviderId !=0) {
            stringBuffer.append(" and p.id = "+queryProviderId);
        }
        if (queryIsPayment !=0){
            stringBuffer.append(" and b.isPayment = "+queryIsPayment);
        }
        stringBuffer.append(" order by b.creationDate desc");
        String sql = stringBuffer.toString();
        //调用dao层查询方法
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        while (resultSet.next()){
            totalCount = resultSet.getInt("totalCount");
        }
        return totalCount;
    }

    @Override
    public boolean delBill(Connection connection, int billId) throws SQLException {
        PreparedStatement preparedStatement = null;
        int execute = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql = "delete from smbms_bill where id = ?";
        //设置占位符的值
        Object[] params = {billId};
        //执行
        execute = BaseDao.execute(connection,sql,params,preparedStatement);
        if (execute > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

    @Override
    public boolean add(Connection connection, Bill bill) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int execute = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql = "insert into smbms_bill(billCode,productName,productUnit,productCount,totalPrice,providerId,isPayment,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?)";
        //设置占位符的值
        Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getProviderId(),bill.getIsPayment(),bill.getCreatedBy(),bill.getCreationDate()};
        //执行
        execute = BaseDao.execute(connection,sql,params,preparedStatement);
        if (execute >0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

    @Override
    public boolean getBillByBillCode(Connection connection, String billCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int billCount = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql = "select count(1) as billCount from smbms_bill where billCode = ?";
        //设置占位符的值
        Object[] params = {billCode};
        //执行
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            billCount = resultSet.getInt("billCount");
        }
        if (billCount > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

}
