package cn.zeong.service.Role;

import cn.zeong.dao.BaseDao;
import cn.zeong.dao.Role.IRoleDAO;
import cn.zeong.dao.Role.RoleDAOImpl;
import cn.zeong.pojo.Role;
import cn.zeong.service.Role.IRoleService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements IRoleService {
    //导入dao层
    private IRoleDAO RoleDAO;
    public RoleServiceImpl(){
        RoleDAO = new RoleDAOImpl();
    }
    @Override
    public List<Role> getAllRole() throws SQLException {
        Connection connection = null;
        //连接数据库
        connection = BaseDao.getConnection();
        //调用dao层的获取所有角色的方法
        List<Role> roleList = RoleDAO.getRoleList(connection);
        //关闭资源
        BaseDao.close(connection,null,null);
        return roleList;
    }

    @Override
    public int getTotalCount(String queryname, int queryUserRole) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的获取总数量的方法
        int totalCount = RoleDAO.getTotalCount(connection, queryname, queryUserRole);
        //关闭资源
        BaseDao.close(connection,null,null);
        return totalCount;
    }

    @Override
    public List<Role> getRoleListById(int userRole) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层
        List<Role> roleListById = RoleDAO.getRoleListById(connection, userRole);
        //关闭资源
        BaseDao.close(connection,null,null);
        return roleListById;
    }
}
