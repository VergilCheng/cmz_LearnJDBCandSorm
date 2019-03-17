package JDBC;

import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * - BLOB(Binary Large Object)
 * - 用于存储大字节数据
 * - 大字节有些特殊，不同数据库处理方式不一样，大字段的操作常常以流的方式来处理，而非一般的字段，一次即可读出数据。
 */
public class Demo09 {

    @Test
    public void test1() {
        Connection connection = null;

        try {
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");

            String sql = "insert into t_user (username,headImg) VALUES (?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "刘涛");

            //将字节文件输入到数据库中
            preparedStatement.setBlob(2,new BufferedInputStream(new FileInputStream("F:/vcredist.bmp")));
            preparedStatement.executeUpdate();


            //将大字节文件从数据库中取出来
            preparedStatement = connection.prepareStatement("select * from t_user where id=20031");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Blob headImg = resultSet.getBlob("headImg");
            InputStream binaryStream = headImg.getBinaryStream();
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("F:/vcredist_copy.bmp"));
            byte[] buffer = new byte[10];
            while (binaryStream.read(buffer,0,buffer.length) != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
            }
            outputStream.close();
            binaryStream.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
