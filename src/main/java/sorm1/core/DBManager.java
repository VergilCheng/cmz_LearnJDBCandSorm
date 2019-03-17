package sorm1.core;

import sorm1.bean.Configuration;
import sorm1.pool.DBConnectionPool;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 *
 * 优化：1.新增连接池对象管理连接 ，提高效率
 *       2.新增getConn方法，从池中获取连接，而不是创建连接
 *       3.修改close方法，用池关闭连接，而不是直接调用Connection的close方法
 */
@SuppressWarnings("all")
public class DBManager {
    /**
     * 配置信息对象
     */
    private static Configuration conf;

    /**
     * 连接池
     */
    private static DBConnectionPool pool;

    static{
        //加载指定的资源文件
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db1.properties"));
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
        conf.setQueryClass(properties.getProperty("queryClass"));
        conf.setPoolMaxSize(Integer.parseInt(properties.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(properties.getProperty("poolMinSize")));

    }

    /**
     * 创建连接对象
     * @return Connection
     */
    public static Connection createConn(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(),
                    conf.getUser(),conf.getPwd());     //直接建立连接，后期增加连接池处理，提高效率！！！
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从连接池中获取连接对象
     * @return Connection
     */
    public static Connection getConn() {
        if (pool == null) {
            //创建连接池
            /*
                注：不能将创建连接池对象放在static块中进行初始化，如果放在static块中，则在加载DBManager类的
                时候，会因为要创建DBConnectionPool对象而先加载DBConnectionPool类，而在DBConnectionPool的常量加载过程中
                是通过DBManager类来对常量进行初始化的，而这时DBManager类还在加载，并没有生成，所以会有空指针异常。
             */
            pool = new DBConnectionPool();
        }
       return pool.getConnection();
    }

    /**
     * 关闭传入的连接
     * @param rs ResultSet
     * @param ps Statement
     * @param conn Connection
     */
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
       /* try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
       pool.closeConnection(conn);

    }
    /**
     * 关闭传入的连接
     * @param ps Statement
     * @param conn Connection
     */
    public static void close(Statement ps,Connection conn){
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       /* try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
       pool.closeConnection(conn);

    }

    /**
     * 关闭传入的连接

     * @param conn Connection
     */
    public static void close(Connection conn){
        /*try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        pool.closeConnection(conn);

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
        Connection conn = DBManager.createConn();
        System.out.println(conn);
    }
}
