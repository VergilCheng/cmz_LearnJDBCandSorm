package JDBC;

import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * - CLOB(Character Large Object)
 * - 用于存储大量的文本数据
 * - 大字段有些特殊，不同数据库处理方式不一样，大字段的操作常常以流的方式来处理，而非一般的字段，一次即可读出数据。
 */
public class Demo08 {

    @Test
    public void test1() {
        Connection connection = null;

        try {
            Class driver = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc", "root", "123asd,./");

            String sql = "insert into t_user (username,myInfo) VALUES (?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "赵强");

            //将文本文件输入到数据库中
            preparedStatement.setClob(2,new FileReader(new File("readme.txt")));
            preparedStatement.executeUpdate();


            //将大文本从数据库中取出来
            preparedStatement = connection.prepareStatement("select * from t_user where id=20027");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Clob myInfo = resultSet.getClob("myInfo");
            Reader characterStream = myInfo.getCharacterStream();
            char[] buffer = new char[5];
            while (characterStream.read(buffer,0,buffer.length) != -1) {
                System.out.print(new String(buffer,0,buffer.length));
            }
            characterStream.close();
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
