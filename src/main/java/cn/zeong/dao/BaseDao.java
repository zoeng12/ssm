package cn.zeong.dao;

import cn.zeong.pojo.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BaseDao {
    //声明连接数据库用到常量
    public static String driver;
    public static String url;
    public static String username;
    public static String password;
    //静态代码块，类加载的时候将初始化
    static {
        //创建properties对象
        Properties pr = new Properties();
        //获取配置文件的流
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        //加载配置文件
        try {
            pr.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取配置文件的内容
        driver = pr.getProperty("driver");
        url = pr.getProperty("url");
        username = pr.getProperty("username");
        password = pr.getProperty("password");
    }

    //连接数据库
    public static Connection getConnection() {
        //加载驱动
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //连接指定数据库
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    //查询数据操作公共方法
    public static ResultSet execute(Connection connection,String sql,Object[] params,PreparedStatement preparedStatement,ResultSet resultSet) throws SQLException {
        //预编译SQL语句
       preparedStatement = connection.prepareStatement(sql);
        //设置占位符的值
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
        //执行SQL语句
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //增删改操作公共方法
    public static int execute(Connection connection,String sql,Object[] params,PreparedStatement preparedStatement) throws SQLException {
        //预编译SQL语句
         preparedStatement = connection.prepareStatement(sql);
         //设置占位符的值
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
        //执行SQL语句
        int rows = preparedStatement.executeUpdate();
        return rows;
    }

    //释放资源
    public static boolean close(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag = true;
        //判断resultset是否为空，若不为空则关闭资源且回收
        if (resultSet!=null){
            try {
                resultSet.close();
                //GC回收
                resultSet=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        //判断preparedstatement是否为空，若不为空则关闭资源且回收
        if (preparedStatement!=null){
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        //判断connection是否为空，若不为空则关闭资源且回收
        if (connection!=null){
            try {
                connection.close();
                //GC回收
                connection = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
