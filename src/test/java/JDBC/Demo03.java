package JDBC;

import java.sql.*;

/**
 * 测试PreparedStatement
 */
public class Demo03 {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");
            //获取Preparedstatement对象,防止sql注入
            String sql = "insert into t_user (username,pwd,regTime) values (?,?,?)";//?占位符
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "hw");//参数索引从1开始计算，而不是0
            preparedStatement.setString(2, "123");
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            //也可以setObject，可以插入任何类型数据
            preparedStatement.setObject(3, new Timestamp(System.currentTimeMillis()));
            //执行
            int i = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
