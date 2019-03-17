package JDBC;

import java.sql.*;

/**
 * 测试ResultSet
 */
public class Demo04 {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");

            String sql = "select * from  t_user where id>?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, 2);
            //返回结果集
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //获取结果集中的列
                System.out.print(resultSet.getObject(1)+"\t");
                System.out.print(resultSet.getObject(2)+"\t");
                System.out.print(resultSet.getObject(3)+"\t");
                System.out.println(resultSet.getObject(4));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
