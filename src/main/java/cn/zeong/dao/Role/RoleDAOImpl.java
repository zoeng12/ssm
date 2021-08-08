package cn.zeong.dao.Role;

import cn.zeong.dao.BaseDao;
import cn.zeong.dao.Role.IRoleDAO;
import cn.zeong.pojo.Role;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements IRoleDAO {
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Role> roleList = new ArrayList<>();
        Object[] params = {};
        //要执行的SQL语句
        String sql = "select * from smbms_role";
        //调用baseDao的查询方法
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        while (resultSet.next()) {
            Role role = new Role();
            role.setId(resultSet.getInt("id"));
            role.setRoleCode(resultSet.getString("roleCode"));
            role.setRoleName(resultSet.getString("roleName"));
            role.setCreateBy(resultSet.getInt("createdBy"));
            role.setCreationDate(resultSet.getDate("creationDate"));
            role.setModifyBy(resultSet.getInt("modifyBy"));
            role.setModifyDate(resultSet.getDate("modifyDate"));
            roleList.add(role);
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return roleList;
    }

    @Override
    public int getTotalCount(Connection connection,String queryname,int queryUserRole) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {};
        int totalCount = 0;
        StringBuffer stringBuffer = new StringBuffer();
        //要执行的SQL语句
        stringBuffer.append("select count(1) as totalCount from smbms_user u,smbms_role r where u.userRole=r.id");
        if (queryname!=null && !StringUtils.isNullOrEmpty(queryname)){
            stringBuffer.append(" and u.userName like "+"'%"+queryname+"%'");
        }
        if (queryUserRole!=0){
            stringBuffer.append(" and r.id = "+"'"+queryUserRole+"'");
        }
        String sql = stringBuffer.toString();
        //调用baseDao层执行sql
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            totalCount = resultSet.getInt("totalCount");
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return totalCount;
    }

    @Override
    public List<Role> getRoleListById(Connection connection, int userRole) throws SQLException {
        ArrayList<Role> roleList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = null;
        Object[] params = {};
        //将要执行的SQL语句
        if (userRole == 1) {
            sql = "select * from smbms_role where id in (?,?,?)";
            params = new Object[]{1, 2, 3};
        }else if (userRole == 2){
            sql = "select * from smbms_role where id in (?)";
            params = new Object[]{3};
        }
        //执行sql
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            Role role = new Role();
            //封装角色
            role.setId(resultSet.getInt("id"));
            role.setRoleCode(resultSet.getString("roleCode"));
            role.setRoleName(resultSet.getString("roleName"));
            role.setCreateBy(resultSet.getInt("createdBy"));
            role.setCreationDate(resultSet.getDate("creationDate"));
            role.setModifyBy(resultSet.getInt("modifyBy"));
            role.setModifyDate(resultSet.getDate("modifyDate"));
            roleList.add(role);
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return roleList;
    }
}
