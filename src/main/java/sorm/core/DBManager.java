package sorm.core;

import sorm.bean.Configuration;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 */
public class DBManager {

    private static Configuration conf;


    static{
        //加载指定的资源文件
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据Properties对象构建Configuration对象
        conf = new Configuration();
        conf.setDriver(properties.getProperty("driver"));
        conf.setPoPackage(properties.getProperty("poPackage"));
        conf.setPwd(properties.getProperty("pwd"));
        conf.setSrcPath(properties.getProperty("srcPath"));
        conf.setUrl(properties.getProperty("url"));
        conf.setUser(properties.getProperty("user"));
        conf.setUsingDB(properties.getProperty("usingDB"));
    }


    public static Connection getConn(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(),
                    conf.getUser(),conf.getPwd());     //直接建立连接，后期增加连接池处理，提高效率！！！
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void close(ResultSet rs, Statement ps, Connection conn){
        try {
            if(rs!=null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement ps,Connection conn){
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(Connection conn){
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回Configuration对象
     * @return
     */
    public static Configuration getConf(){
        return conf;
    }

    /**
     * 测试是是否能够得到封装好的properties属性以及是否能够根据其得到数据库连接对象
     */
    public static void main(String[] args) {
        Configuration conf = DBManager.getConf();
        System.out.println(conf);
        Connection conn = DBManager.getConn();
        System.out.println(conn);
    }
}
