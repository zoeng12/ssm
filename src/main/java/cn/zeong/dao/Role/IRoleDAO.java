package cn.zeong.dao.Role;

import cn.zeong.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IRoleDAO {
    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException;
    //获取总数量
    public int getTotalCount(Connection connection,String queryname,int queryUserRole) throws SQLException;
    //通过id获取角色列表
    public List<Role> getRoleListById(Connection connection,int Id) throws SQLException;
}
