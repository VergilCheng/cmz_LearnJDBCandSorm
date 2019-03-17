package sorm1.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 方法回调接口
 *
 * 对不同的DQL语句（多行多列，单行单列，单行多列）提供实现接口
 */
public interface CallBack {
    /**
     *
     * 执行不同的DQL语句（多行多列，单行单列，单行多列）的不同细节
     *
     * @param connection 数据库连接
     * @param preparedStatement PrepareStatement
     * @param resultSet ResultSet
     * @param clazz 字段或者pojo类的class对象
     * @return 结果集
     */
    List doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, Class clazz);


}
