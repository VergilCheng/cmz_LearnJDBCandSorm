package JDBC;

import java.sql.*;

/**
 * 批量处理
 *
 * 注意：对于大批量处理，建议使用Statement，因为PreparedStatement的预编译空间有限，当数据量非常大的时候，会发生异常
 */
public class Demo05 {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");

            //将事务提交变为手动提交
            connection.setAutoCommit(false);

            long start = System.currentTimeMillis();
            String sql = "select * from  t_user where id>?";
            Statement statement = connection.createStatement();
            //批量处理
            for (int i = 0; i <20000 ; i++) {
                statement.addBatch("insert into t_user2(username,pwd,regTime) values ('gaoqi" + i + "',666666,now())");
            }
            statement.executeBatch();
            connection.commit();
            long end = System.currentTimeMillis();
            System.out.println("耗时："+(end-start));


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
