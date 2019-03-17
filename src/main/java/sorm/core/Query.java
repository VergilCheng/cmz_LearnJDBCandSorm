package sorm.core;

import java.util.List;

/**
 * 负责DML和DQL（对外提供服务的核心类）
 *
 * @author cmz
 */
public interface Query {
    /**
     * 直接执行一个DML语句:包括update，delete，以及insert
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 执行sql语句后影响了几行记录
     */
    int excuteDML(String sql, Object[] params);

    /**
     * 将一个对象存储到数据库中
     *
     * 把对象中不为null的属性往数据库中存储
     * @param object 需要存储的对象
     * @return 插入了几条记录
     */
    int insert(Object object);

    /**
     * 删除clazz表示类对应中的表的记录（指定主键id的记录）
     * @param clazz 跟表对应的类的对象
     * @param id 主键id
     * @return 删除了一条记录
     */
    int delete(Class clazz,Object id);

    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应到表，对象的主键值对应到记录）
     *
     * dml条件为：where 主键=？
     * @param object 应当删除的对应数据库中的对象
     * @return 删除了几条记录
     */
    int delete(Object object);

    /**
     * 更新对象对应的记录，并只更新指定的字段值
     *
     * dml条件为：where 主键=？
     *
     * @param object 所有要更新的对象
     * @param filedNames 更新的属性列表
     * @return 影响了几行记录
     */
    int update(Object object,String... filedNames);

    /**
     * 根据传入对象来更新数据库中的记录
     *
     * dml条件为：where 主键=？
     *
     * @param object 需要更新的对象
     * @return 更新了一条记录
     */
    int update(Object object);

    /**
     * 查询返回多行记录，并且将每行记录封装到clazz指定的类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的javaBean类的Class对象
     * @param params sql的参数（DQL语句查询条件）
     * @return 查询到的结果
     */
    List queryRows(String sql, Class clazz, Object... params);

    /**
     * 查询返回一行记录，并且将每行记录封装到clazz指定的类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的javaBean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    Object queryUniqueRow(String sql, Class clazz, Object... params);


    /**
     * 查询返回一个列（一列），并且将每行记录封装到clazz指定的类的对象中
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    List queryValue(String sql, Class clazz, Object... params);


    /**
     * 查询返回一列数字，并且将每行记录封装到clazz指定的类的对象中
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    List<Number> queryNumber(String sql, Class clazz, Object... params);
}
