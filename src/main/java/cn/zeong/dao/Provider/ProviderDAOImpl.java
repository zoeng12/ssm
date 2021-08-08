package cn.zeong.dao.Provider;

import cn.zeong.dao.BaseDao;
import cn.zeong.pojo.Provider;
import com.mysql.jdbc.StringUtils;

import javax.jws.Oneway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDAOImpl implements IProviderDAO{

    @Override
    public List<Provider> getProviderList(Connection connection,String queryProCode,String queryProName,int currentPageNo,int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Provider> providerList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        //将要执行的SQL语句
        stringBuffer.append("select * from smbms_provider");
        if (queryProCode != null && queryProName == null){
            stringBuffer.append(" where proCode like "+"'%"+queryProCode+"%'");
        }
        if (queryProCode == null && queryProName != null){
            stringBuffer.append(" where proName like "+"'%"+queryProName+"%'");
        }
        if (queryProCode != null && queryProName != null){
            stringBuffer.append(" where proCode like "+"'%"+queryProCode+"%'"+" and proName like "+"'%"+queryProName+"%'");
        }
        stringBuffer.append(" order by creationDate desc limit ?,?");
        //起始索引
        currentPageNo = (currentPageNo-1)*pageSize;
        Object[] params = {currentPageNo,pageSize};
        String sql = stringBuffer.toString();
        //执行
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            Provider provider = new Provider();
            provider.setProCode(resultSet.getString("proCode"));
            provider.setProName(resultSet.getString("proName"));
            provider.setProContact(resultSet.getString("proContact"));
            provider.setProPhone(resultSet.getString("proPhone"));
            provider.setProFax(resultSet.getString("proFax"));
            provider.setCreationDate(resultSet.getDate("creationDate"));
            provider.setId(resultSet.getInt("id"));
            providerList.add(provider);
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return providerList;
    }

    @Override
    public int totalCount(Connection connection,String queryProCode,String queryProName) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {};
        int totalCount = 0;
        //将要执行的SQL语句
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select count(1) as totalCount from smbms_provider");
        if (queryProCode != null && queryProName == null){
            stringBuffer.append(" where proCode like "+"'%"+queryProCode+"%'");
        }
        if (queryProCode == null && queryProName != null){
            stringBuffer.append(" where proName like "+"'%"+queryProName+"%'");
        }
        if (queryProCode != null && queryProName != null){
            stringBuffer.append(" where proCode like "+"'%"+queryProCode+"%'"+" and proName like "+"'%"+queryProName+"%'");
        }
        String sql = stringBuffer.toString();
        //执行
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            totalCount = resultSet.getInt("totalCount");
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return totalCount;
    }

    @Override
    public Provider getProviderById(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Provider provider = new Provider();
        //将要执行的SQL语句
        String sql = "select * from smbms_provider where id = ?";
        //设置占位符的值
        Object[] params = {id};
        //执行
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            provider.setProCode(resultSet.getString("proCode"));
            provider.setProName(resultSet.getString("proName"));
            provider.setProContact(resultSet.getString("proContact"));
            provider.setProPhone(resultSet.getString("proPhone"));
            provider.setProAddress(resultSet.getString("proAddress"));
            provider.setProFax(resultSet.getString("proFax"));
            provider.setProDesc(resultSet.getString("proDesc"));
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return provider;
    }

    @Override
    public boolean updateProvider(Connection connection, Provider provider) throws SQLException {
        PreparedStatement preparedStatement = null;
        int execute = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql = "update smbms_provider set proName = ?,proContact = ?,proPhone = ?,proAddress = ?,proFax = ?,proDesc = ?,modifyBy = ?,modifyDate = ? where id = ?";
        //设置占位符的值
        Object[] params = {provider.getProName(),provider.getProContact(),provider.getProPhone(),provider.getProAddress(),provider.getProFax(),provider.getProDesc(),provider.getModifyBy(),provider.getModifyDate(),provider.getId()};
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
    public boolean delProvider(Connection connection, int id) throws SQLException {
        int execute = 0;
        int proBill = 0;
        boolean flag = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //将要执行的SQL语句
        String sql1 = "select count(1) as proBill from smbms_bill where providerId = ?";
        String sql = "delete from smbms_provider where id = ?";
        //设置占位符的值
        Object[] params1 = {id};
        Object[] params = {id};
        //执行
        resultSet = BaseDao.execute(connection,sql1,params1,preparedStatement,resultSet);
        while (resultSet.next()){
            proBill = resultSet.getInt("proBill");
        }
        if (proBill > 0){
            flag = false;
        }else {
            //执行
            execute = BaseDao.execute(connection,sql,params,preparedStatement);
            if (execute > 0){
                flag = true;
            }
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

    @Override
    public boolean addProvider(Connection connection, Provider provider) throws SQLException {
        boolean flag = false;
        int execute = 0;
        PreparedStatement preparedStatement = null;
        //将要执行的SQL语句
        String sql = "insert into smbms_provider(proCode,proName,proContact,proPhone,proAddress,proFax,proDesc,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?)";
        //设置占位符的值
        Object[] params = {provider.getProCode(),provider.getProName(),provider.getProContact(),provider.getProPhone(),provider.getProAddress(),provider.getProFax(),provider.getProDesc(),provider.getCreateBy(),provider.getCreationDate()};
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
    public boolean pcExist(Connection connection, String proCode) throws SQLException {
        int proCodeCount = 0;
        boolean flag = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //将要执行的SQL语句
        String sql = "select count(1) as proCodeCount from smbms_provider where proCode = ?";
        //设置占位符
        Object[] params = {proCode};
        //执行
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            proCodeCount = resultSet.getInt("proCodeCount");
        }
        if (proCodeCount > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return flag;
    }


}
