package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 测试事务
 */
public class Demo06 {


    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {

            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");

            connection.setAutoCommit(false);

            String sql = "insert into t_user(username,pwd) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, "高琪");
            preparedStatement.setObject(2, "1234577");

            preparedStatement.executeUpdate();

            Thread.sleep(6000);

            PreparedStatement preparedStatement2 = connection.prepareStatement("insert into t_user(username,pwd) values(?,?,?)");//故意报错

            preparedStatement2.setObject(1, "马士兵");
            preparedStatement2.setObject(2, "1234577");
            preparedStatement2.executeUpdate();

            connection.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } catch (InterruptedException e) {
            e.printStackTrace();
            connection.rollback();
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
