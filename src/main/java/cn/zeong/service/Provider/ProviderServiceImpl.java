package cn.zeong.service.Provider;

import cn.zeong.dao.BaseDao;
import cn.zeong.dao.Provider.IProviderDAO;
import cn.zeong.dao.Provider.ProviderDAOImpl;
import cn.zeong.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements IProviderService{
    //导入dao层
    private IProviderDAO providerDAO;
    public ProviderServiceImpl(){
        providerDAO = new ProviderDAOImpl();
    }
    @Override
    public List<Provider> getProviderList(String queryProCode,String queryProName,int currentPageNo,int pageSize) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的获取供应商列表的方法
        List<Provider> providerList = providerDAO.getProviderList(connection,queryProCode,queryProName,currentPageNo,pageSize);
        //关闭资源
        BaseDao.close(connection,null,null);
        return providerList;
    }

    @Override
    public int totalCount(String queryProCode, String queryProName) throws SQLException {
        int totalCount = 0;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的获取供应商的总数量
        totalCount = providerDAO.totalCount(connection,queryProCode,queryProName);
        //关闭资源
        BaseDao.close(connection,null,null);
        return totalCount;
    }

    @Override
    public Provider getProviderById(int id) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层通过id获取供应商信息的方法
        Provider provider = providerDAO.getProviderById(connection, id);
        //关闭资源
        BaseDao.close(connection,null,null);
        return provider;
    }

    @Override
    public boolean updateProvider(Provider provider) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始业务处理
        connection.setAutoCommit(false);
        //调用dao层的通过id修改供应商的方法
        flag = providerDAO.updateProvider(connection,provider);
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
    public boolean delProvider(int id) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始业务处理
        connection.setAutoCommit(false);
        //调用dao层的通过id删除供应商的方法
        flag = providerDAO.delProvider(connection,id);
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
    public boolean addProvider(Provider provider) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始业务处理
        connection.setAutoCommit(false);
        //调用dao层的添加供应商的方法
        flag = providerDAO.addProvider(connection,provider);
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
    public boolean pcExist(String proCode) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的判断供应商编码是否存在的方法
        flag = providerDAO.pcExist(connection,proCode);
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

}
