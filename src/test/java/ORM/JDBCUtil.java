package ORM;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * JDBCUtil
 */
@SuppressWarnings("all")
public class JDBCUtil {

    static Properties properties;

    static {//写在静态块之中，那么只会加载一次，不会加载多次，节省资源
        properties = new Properties();
        try {
            // 注意：
            // 由于我们test文件夹中的配置文件不在src目录下，所以不能使用
            // Thread.currentThread().getContextClassLoader().getResourceAsStream("db_test.properties")
            // 因为这个类加载器只能加载当前项目src目录下的文件，所以我们只能用FileInputStream！！！
            properties.load(new FileInputStream("F:/my code/src/test/java/JDBC/db_test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getMysqlConnection() {
        try {
            Class.forName(properties.getProperty("driver"));
            Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("pwd"));
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {

        }
        return null;
    }

    /**
     * 关闭连接
     * @param connection 连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接与statement
     *
     * 关闭顺序:ResultSet——>Statement——>Connection
     * @param connection
     * @param statement
     */
    public static void closeConnection(Connection connection,Statement statement) {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接，statement，resultSet
     *
     * @param connection 连接
     */
    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        Connection mysqlConnection = JDBCUtil.getMysqlConnection();
        System.out.println(mysqlConnection);
        JDBCUtil.closeConnection(mysqlConnection);
    }

}
