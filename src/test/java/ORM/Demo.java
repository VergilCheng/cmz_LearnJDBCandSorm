package ORM;


import ORM.pojo.Emp;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Demo {

    /**
     * 测试使用Object数组来封装一条！！记录
     * 使用List存储多条Object数组封装的记录
     */
    @Test
    public void test1() {
        Connection connection = JDBCUtil.getMysqlConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from Emp where id >=?";
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, 1);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] columns = new Object[5];
                columns[0] = resultSet.getObject("ename");
                columns[1] = resultSet.getObject("salary");
                columns[2] = resultSet.getObject("birthday");
                columns[3] = resultSet.getObject("age");
                columns[4] = resultSet.getObject("deptId");
                list.add(columns);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(connection,preparedStatement,resultSet);
            //在连接中断后，数据依然保存在Object数组中，不会因为连接中断而丢失！！
            for (Object[] objs : list) {
                for (int i = 0; i < objs.length; i++) {
                    System.out.print(objs[i] + "\t");
                }
                System.out.println();
            }
        }

    }
    /**
     * 使用Map封装一条记录
     */
    @Test
    public void test2() {
        Connection connection = JDBCUtil.getMysqlConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from Emp where id >=?";
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ename", resultSet.getObject("ename"));
                map.put("salary", resultSet.getObject("salary"));
                map.put("birthday", resultSet.getObject("birthday"));
                map.put("age", resultSet.getObject("age"));
                map.put("deptId", resultSet.getObject("deptId"));
                list.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(connection,preparedStatement,resultSet);

            for (Map<String, Object> map : list) {
                System.out.println(map);
            }

        }
    }


    /**
     * 使用javaBean封装一条记录
     */
    @Test
    public void test3() {
        Connection connection = JDBCUtil.getMysqlConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from Emp where id >=?";
        List<Emp> list = new ArrayList<Emp>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Emp emp = new Emp();
                emp.setEname((String) resultSet.getObject("ename"));
                emp.setSalary((Double) resultSet.getObject("salary"));
                emp.setBirthday((Date) resultSet.getObject("birthday"));
                emp.setAge((Integer) resultSet.getObject("age"));
                emp.setDeptId((Integer) resultSet.getObject("deptId"));
                list.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(connection,preparedStatement,resultSet);

            for (Emp emp: list) {
                System.out.println(emp);
            }

        }
    }


}
