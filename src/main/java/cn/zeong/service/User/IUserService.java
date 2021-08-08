package cn.zeong.service.User;

import cn.zeong.pojo.Role;
import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;

import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IUserService {
    public User login(String userCode,String password);
    public boolean updatePwd(Integer id,String newPwd) throws SQLException;
    public List<User> getAllUser(String queryname,int queryUserRole,int currentPage,int pageSize) throws SQLException;
    public User getUserById(int Id) throws SQLException;
    public boolean updateUserById(User user,int modifyBy) throws SQLException;
    public boolean delUserById(int Id) throws SQLException;
    public boolean getUserByUserCode(String userCode) throws SQLException;
    public boolean insertUser(User user) throws SQLException;
    public boolean updateSession(Session session) throws SQLException;
//    public boolean delSession(String sessionId) throws SQLException;
    public Session getSessionList(int id) throws SQLException;
}
