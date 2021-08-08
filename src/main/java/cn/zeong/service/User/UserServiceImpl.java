package cn.zeong.service.User;

import cn.zeong.dao.BaseDao;
import cn.zeong.dao.User.IUserDAO;
import cn.zeong.dao.User.UserDAOImpl;
import cn.zeong.pojo.Role;
import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements IUserService{
    //导入dao层
    private IUserDAO userDAO;
    public UserServiceImpl(){
        userDAO = new UserDAOImpl();
    }
    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;
        //连接数据库
        connection = BaseDao.getConnection();
        //获取user的信息
        try {
            user = userDAO.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源
            BaseDao.close(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean updatePwd(Integer id, String newPwd) throws SQLException {
        Connection connection = null;
        Boolean flag = false;
        //连接数据库
        connection = BaseDao.getConnection();
        //开始事务处理
        connection.setAutoCommit(false);
        //调用dao层的更新方法
        flag = userDAO.updatePwd(connection, id, newPwd);
        if (flag){
            connection.commit();
        }else {
            connection.rollback();
        }
        BaseDao.close(connection,null,null);
        return flag;
    }


    @Override
    public List<User> getAllUser(String queryname,int queryUserRole,int currentPage,int pageSize) throws SQLException {
        Connection connection = null;
        //连接数据库
        connection = BaseDao.getConnection();
        //调用查询所有用户的方法
        List<User> userList = userDAO.getUserList(connection,queryname,queryUserRole,currentPage,pageSize);
        //关闭资源
        BaseDao.close(connection,null,null);
        return userList;
    }

    @Override
    public User getUserById(int Id) throws SQLException {
        User user = new User();
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的通过id查询用户的方法
        user = userDAO.getUserById(connection, Id);
        //关闭资源
        BaseDao.close(connection,null,null);
        return user;
    }

    @Override
    public boolean updateUserById(User user,int modifyBy) throws SQLException {
        Connection connection = null;
        boolean flag = false;
        //连接数据库
        connection = BaseDao.getConnection();
        //开始事务处理
        connection.setAutoCommit(false);
        //调用dao层的通过id修改用户数据的方法
        flag = userDAO.updateUserById(connection,user,modifyBy);
        if (flag) {
            connection.commit();
        }else {
            connection.rollback();
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

    @Override
    public boolean delUserById(int Id) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始事务处理
        connection.setAutoCommit(false);
        //调用dao层的删除用户的方法
        flag = userDAO.delUserById(connection,Id);
        if (flag){
            connection.commit();
            flag = true;
        }else {
            connection.rollback();
        }
        return flag;
    }

    @Override
    public boolean getUserByUserCode(String userCode) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的通过userCode查询的方法
        flag = userDAO.getUserByUserCode(connection,userCode);
        if (flag){
            flag = true;
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

    @Override
    public boolean insertUser(User user) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //开始事务处理
        connection.setAutoCommit(false);
        //调用dao层
        flag = userDAO.insertUser(connection,user);
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
    public boolean updateSession( Session session) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的插入session数据的方法
        flag = userDAO.updateSession(connection,session);
        //开始业务处理
        connection.setAutoCommit(false);
        if (flag){
            connection.commit();
        }else {
            connection.rollback();
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

    /*
    @Override
    public boolean delSession(String sessionId) throws SQLException {
        boolean flag = false;
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用dao层的插入session数据的方法
        flag = userDAO.delSession(connection,sessionId);
        //开始业务处理
        connection.setAutoCommit(false);
        if (flag){
            connection.commit();
        }else {
            connection.rollback();
        }
        //关闭资源
        BaseDao.close(connection,null,null);
        return flag;
    }

     */

    @Override
    public Session getSessionList(int id) throws SQLException {
        //连接数据库
        Connection connection = BaseDao.getConnection();
        //调用service层的查询session数据的方法
        Session session = userDAO.getSessionList(connection,id);
        //关闭资源
        BaseDao.close(connection,null,null);
        return session;
    }
}
