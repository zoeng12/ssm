package cn.zeong.dao.User;

import cn.zeong.dao.BaseDao;
import cn.zeong.pojo.Role;
import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;
import com.mysql.jdbc.StringUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAOImpl implements IUserDAO {
    @Override
    public User getLoginUser(Connection connection, String userCode) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        //根据userCode查询用户
        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};
            //执行SQL语句
            try {
                resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
                //将获得的数据存进user中
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUserCode(resultSet.getString("userCode"));
                    user.setUserName(resultSet.getString("userName"));
                    user.setUserPassword(resultSet.getString("userPassword"));
                    user.setGender(resultSet.getInt("gender"));
                    user.setBirthday(resultSet.getDate("birthday"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("address"));
                    user.setUserRole(resultSet.getInt("userRole"));
                    user.setCreateBy(resultSet.getInt("createdBy"));
                    user.setCreationDate(resultSet.getDate("creationDate"));
                    user.setModifyBy(resultSet.getInt("modifyBy"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                //关闭资源
                BaseDao.close(null, preparedStatement, resultSet);
            }
        }
        return user;
    }

    @Override
    public boolean updatePwd(Connection connection, Integer id, String newPwd) {
        //设置一个标杆
        boolean flag = false;
        int execute = 0;
        PreparedStatement preparedStatement = null;
        if (connection != null) {
            //需要执行的SQL语句
            String sql = "update smbms_user set userPassword = ? where id = ?";
            //设置占位符的值
            Object[] params = {newPwd, id};
            //调用更新公共方法
            try {
                execute = BaseDao.execute(connection, sql, params, preparedStatement);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                BaseDao.close(null, preparedStatement, null);
            }
            //如果执行返回的值大于0，则表示更新成功
            if (execute > 0) {
                flag = true;
            }
        }
        return flag;
    }

    public List<User> getUserList(Connection connection, String queryname, int queryUserRole, int currentPage, int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<User> userList = new ArrayList<>();
        //要执行的sql语句
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select * from smbms_user u,smbms_role r where u.userRole = r.id");
        if (queryname != null && !StringUtils.isNullOrEmpty(queryname)) {
            stringBuffer.append(" and u.username like " + "'%" + queryname + "%'");
        }
        if (queryUserRole != 0) {
            stringBuffer.append(" and r.id =" + "'" + queryUserRole + "'");
        }
        stringBuffer.append(" order by u.creationDate desc limit ?,?");
        String sql = stringBuffer.toString();
        //设置占位符数值,第一个？-->当前页(currentPage-1)*页面大小pageSize,第二个？-->pageSize
        int pageIndex = (currentPage - 1) * pageSize;
        Object[] params = {pageIndex, pageSize};
        //调用dao层查询方法
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUserCode(resultSet.getString("userCode"));
            user.setUserName(resultSet.getString("userName"));
            user.setUserPassword(resultSet.getString("userPassword"));
            user.setGender(resultSet.getInt("gender"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setPhone(resultSet.getString("phone"));
            user.setAddress(resultSet.getString("address"));
            user.setUserRole(resultSet.getInt("userRole"));
            user.setCreateBy(resultSet.getInt("createdBy"));
            user.setCreationDate(resultSet.getDate("creationDate"));
            user.setModifyBy(resultSet.getInt("modifyBy"));
            user.setUserRoleName(resultSet.getString("roleName"));
            userList.add(user);
        }
        //关闭资源
        BaseDao.close(null, preparedStatement, resultSet);
        return userList;
    }

    @Override
    public User getUserById(Connection connection, int Id) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //将要执行的SQL语句
        String sql = "select * from smbms_user u,smbms_role r where u.userRole = r.id and u.id = ?";
        //设置占位符的值
        Object[] params = {Id};
        //执行sql
        resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        while (resultSet.next()){
            user.setUserCode(resultSet.getString("userCode"));
            user.setUserName(resultSet.getString("userName"));
            user.setGender(resultSet.getInt("gender"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setPhone(resultSet.getString("phone"));
            user.setAddress(resultSet.getString("address"));
            user.setUserRoleName(resultSet.getString("roleName"));
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return user;
    }

    @Override
    public boolean updateUserById(Connection connection,User user,int modifyBy ) throws SQLException {
        Date modifyDate = new Date();
        PreparedStatement preparedStatement = null;
        int execute = 0;
        boolean flag = false;
        //将要执行的sql语句
        String sql1 = "update smbms_user set userName = ?,gender = ?,birthday = ?,phone = ?,address = ?,userRole = ?,modifyBy = ?,modifyDate = ? where id = ?;";
        //设置占位符的值
        Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getUserRole(),modifyBy,modifyDate,user.getId()};
        //执行sql
        execute = BaseDao.execute(connection, sql1, params, preparedStatement);
        if (execute > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

    @Override
    public boolean delUserById(Connection connection, int Id) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int execute = 0;
        int execute2 = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql1 = "delete from smbms_user where id = ?;";
        String sql2 = "DELETE FROM session_account WHERE id = ?;";
        //设置占位符的值
        Object[] params = {Id};
        Object[] params2 = {Id};
        //执行sql
        execute = BaseDao.execute(connection,sql1,params,preparedStatement);
        execute2 = BaseDao.execute(connection,sql2,params2,preparedStatement);
        if (execute > 0 && execute2 >0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return flag;
    }

    @Override
    public boolean getUserByUserCode(Connection connection, String userCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rows = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql = "select count(1) as userCount from smbms_user where userCode = ?";
        //设置占位符的值
        Object[] params = {userCode};
        //执行sql
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            rows = resultSet.getInt("userCount");
        }
        if (rows > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return flag;
    }

    @Override
    public boolean insertUser(Connection connection, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        int execute = 0;
        int execute2 = 0;
        boolean flag = false;
        //将要执行的SQL语句
        String sql1 = "insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?,?);";
        String sql2 = "insert into session_account (id,userCode) select id,userCode from smbms_user ORDER BY creationDate desc LIMIT 1;";
        //设置占位符的值
        Object[] params1 = {user.getUserCode(),user.getUserName(),user.getUserPassword(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getUserRole(),user.getCreateBy(),user.getCreationDate()};
        Object[] params2 = {};
        //执行sql
        execute = BaseDao.execute(connection, sql1, params1, preparedStatement);
        execute2 = BaseDao.execute(connection,sql2,params2,preparedStatement);
        if (execute > 0 && execute2 >0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

    @Override
    public boolean updateSession(Connection connection, Session session) throws SQLException {
        StringBuffer stringBuffer = new StringBuffer();
        PreparedStatement preparedStatement = null;
        Object[] params = {};
        boolean flag = false;
        int execute = 0;
        //将要执行的SQL语句
        if (session.getLoginDate() != null && session.getLogoutDate() == null) {
            stringBuffer.append("update session_account set sessionId = ?,login_date = ? where id = ?");
            params = new Object[]{session.getSessionId(), session.getLoginDate(), session.getId()};
        }else if (session.getLogoutDate() != null && session.getLoginDate() == null){
            stringBuffer.append("update session_account set sessionId = ?,logout_date = ? where id = ?");
            params = new Object[]{session.getSessionId(), session.getLogoutDate(), session.getId()};
        }

        String sql = stringBuffer.toString();
        //执行sql
        execute = BaseDao.execute(connection,sql,params,preparedStatement);
        if (execute > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }

    /*
    @Override
    public boolean delSession(Connection connection,Session session) throws SQLException {
        PreparedStatement preparedStatement = null;
        boolean flag = false;
        int execute = 0;
        //将要执行的SQL语句
        String sql = "delete from session_account where id = ?";
        //设置占位符的值
        Object[] params = {sessionId};
        //执行sql
        execute = BaseDao.execute(connection,sql,params,preparedStatement);
        if (execute > 0){
            flag = true;
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,null);
        return flag;
    }
     */

    @Override
    public Session getSessionList(Connection connection, int id) throws SQLException {
        Session session = new Session();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //将要执行的SQL语句
        String sql = "select * from session_account where id = ?";
        //设置占位符的值
        Object[] params = {id};
        //执行sql
        resultSet = BaseDao.execute(connection,sql,params,preparedStatement,resultSet);
        while (resultSet.next()){
            session.setId(resultSet.getInt("id"));
            session.setSessionId(resultSet.getString("sessionId"));
            session.setUserCode(resultSet.getString("userCode"));
            session.setloginDate(resultSet.getDate("login_date"));
            session.setLogoutDate(resultSet.getDate("logout_date"));
        }
        //关闭资源
        BaseDao.close(null,preparedStatement,resultSet);
        return session;
    }

    @Test
    public void test(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?,?);");
        stringBuffer.append(" insert into session_account (id,userCode) select id,userCode from smbms_user ORDER BY creationDate desc LIMIT 1;");
        String sql = stringBuffer.toString();
        System.out.println(sql);
    }

}
