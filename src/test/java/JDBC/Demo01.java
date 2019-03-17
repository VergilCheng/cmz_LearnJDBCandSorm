package JDBC;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 测试通过jdbc与数据库进行连接
 */
public class Demo01 {


    public static void main(String[] args) {
        Connection connection = null;
        try {
            //通过反射加载驱动类到内存中,就可以不通过import来静态导入了，这样也可以实现解耦，将配置信息写在配置文件中，更改数据库可以更改配置文件
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            //建立连接（连接内部对象包含了Socket对象，是一个远程连接，比较耗时！这是Connection对象管理的一个要点）
            // 真正开发中，为了提高效率，都会使用连接池来管理连接对象
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");
            System.out.println(connection);
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


    @Test
    public void test1() {
        System.out.println("hello world");

    }

}
