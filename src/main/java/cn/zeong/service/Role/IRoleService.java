package cn.zeong.service.Role;

import cn.zeong.pojo.Role;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IRoleService {
    public List<Role> getAllRole() throws SQLException;
    public int getTotalCount(String queryname,int queryUserRole) throws SQLException;
    public List<Role> getRoleListById(int userRole) throws SQLException;
}
