package cn.zeong.service.Bill;

import cn.zeong.dao.BaseDao;
import cn.zeong.dao.Bill.BillDAOImpl;
import cn.zeong.pojo.Bill;
import cn.zeong.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements IBillService{
    private BillDAOImpl billDAO;
    public BillServiceImpl(){
        billDAO = new BillDAOImpl();
    }
    @Override
    public List<Bill> getBillList(String queryProductName,int queryProviderId,int queryIsPayment,int currentPageNo,int pageSize) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用到dao层的获取账单列表的方法
        List<Bill> billList = billDAO.getBillList(connection, queryProductName,queryProviderId,queryIsPayment,currentPageNo,pageSize);
        //关闭资源
        BaseDao.close(connection,null, null);
        return billList;
    }

    @Override
    public List<Provider> getProviderList() throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的获取供应商列表的方法
        List<Provider> providerList = billDAO.getProviderList(connection);
        //关闭资源
        BaseDao.close(connection,null,null);
        return providerList;
    }

    @Override
    public Bill getBillById(int id) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的通过id获取订单信息的方法
        Bill billById = billDAO.getBillById(connection, id);
        //关闭资源
        BaseDao.close(connection,null,null);
        return billById;
    }

    @Override
    public boolean updateBillById(Bill bill) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始业务处理
        connection.setAutoCommit(false);
        //调用dao层的通过id修改订单的方法
        flag = billDAO.updateBillById(connection, bill);
        if (flag){
            connection.commit();
        }else {
            connection.rollback();
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

    @Override
    public int getTotalCount(String queryProductName, int queryProviderId, int queryIsPayment) throws SQLException {
        int totalCount = 0;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的获取订单总数量
        totalCount = billDAO.getTotalCount(connection,queryProductName,queryProviderId,queryIsPayment);
        //关闭资源
        BaseDao.close(connection,null,null);
        return totalCount;
    }

    @Override
    public boolean delBill(int billId) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始业务处理
        connection.setAutoCommit(false);
        //调用dao层的通过id删除订单的方法
        flag = billDAO.delBill(connection,billId);
        if (flag){
            connection.commit();
        }else {
            connection.rollback();
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

    @Override
    public boolean add(Bill bill) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始业务处理
        connection.setAutoCommit(false);
        //调用添加订单的方法
        flag = billDAO.add(connection,bill);
        if (flag){
            connection.commit();
        }else {
            connection.rollback();
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

    @Override
    public boolean getBillByBillCode(String billCode) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的通过订单编码获取订单数据的方法
        flag = billDAO.getBillByBillCode(connection,billCode);
        if(flag){
            flag = true;
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }
}
