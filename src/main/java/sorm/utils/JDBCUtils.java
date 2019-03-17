package sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了JDBC查询常用操作
 */
public class JDBCUtils {

    /**
     * 给sql语句设置参数
     *
     * @param preparedStatement
     * @param params
     */
    public static void handleParams(PreparedStatement preparedStatement, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    preparedStatement.setObject(1 + i, params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}