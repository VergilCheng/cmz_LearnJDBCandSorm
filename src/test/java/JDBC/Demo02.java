package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 测试Statement执行SQL语句以及SQL注入安全问题
 */
public class Demo02 {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");
            //获取statement对象
            Statement statement = connection.createStatement();
            String sql = "insert into t_user(username,pwd,regTime) values('程铭哲','66666',now());";
            statement.execute(sql);
            //测试sql注入造成的安全问题
            String id = "6 or 1=1";
            String sql1 = "delete from t_user where id="+id;
            boolean execute = statement.execute(sql1);//执行过后sql表中数据会消失
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
