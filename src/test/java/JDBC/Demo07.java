package JDBC;

import org.junit.Test;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 测试jdbc的时间类
 */
public class Demo07 {


    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {

            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");


            String sql = "insert into t_user(username,pwd,regTime) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, "高琪");
            preparedStatement.setObject(2, "1234577");
            Date date = new Date(System.currentTimeMillis());
            preparedStatement.setObject(3,date);

            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
        } catch (SQLException e) {
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

    /**
     * 时间段查询操作
     */

    public static long str2Date(String string) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.parse(string).getTime();
    }

    @Test
    public void test1() {
        Connection connection = null;
        try {

            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");


            String sql = "select * from t_user where regTime>? and regTime<?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            Timestamp date1 = new Timestamp(str2Date("2018-09-17 08:00:00"));
            Timestamp date2 = new Timestamp(str2Date("2118-11-15 08:00:00"));
            preparedStatement.setObject(1, date1);
            preparedStatement.setObject(2,date2);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id")+"--"+resultSet.getString("username")+
                        "--"+resultSet.getString("pwd")+"--"+resultSet.getObject("regTime"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ParseException e) {
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
