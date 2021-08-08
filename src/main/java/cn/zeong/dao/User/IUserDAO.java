package cn.zeong.dao.User;

import cn.zeong.pojo.Role;
import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IUserDAO {
    //登录
    public User getLoginUser(Connection connection, String userCode) throws SQLException;
    //修改密码
    public boolean updatePwd(Connection connection,Integer id,String newPwd);
    //获取用户列表
    public List<User> getUserList(Connection connection,String queryname,int queryUserRole,int currentPage,int pageSize) throws SQLException;
    //通过id获取用户数据
    public User getUserById(Connection connection,int Id) throws SQLException;
    //通过id修改用户数据
    public boolean updateUserById(Connection connection,User user,int modifyBy) throws SQLException;
    //通过id删除用户
    public boolean delUserById(Connection connection,int Id) throws SQLException;
    //通过usercode查询用户
    public boolean getUserByUserCode(Connection connection,String userCode) throws SQLException;
    //添加用户
    public boolean insertUser(Connection connection,User user) throws SQLException;
    //保存会话信息
    public boolean updateSession(Connection connection,Session session) throws SQLException;
    //删除会话信息
    //public boolean delSession(Connection connection,String sessionId) throws SQLException;
    //通过sessionId获取会话信息
    public Session getSessionList(Connection connection,int id) throws SQLException;
}
