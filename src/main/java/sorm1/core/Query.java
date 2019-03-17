package sorm1.core;

import sorm1.bean.ColumnInfo;
import sorm1.bean.TableInfo;
import sorm1.utils.JDBCUtils;
import sorm1.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责DML和DQL（对外提供服务的核心类）
 *
 * 优化：1.模板方法模式：将MysqlQuery中的delete，excuteDML这些和数据库类型没有直接关系的方法代码
 *                       移动到Query中，同时接口变抽象类
 *       2.方法回调：对Query中的查询方法（queryRows等方法）进行优化，将得到ResultSet，Connection与PreparedStatement的
 *                   代码进行封装重用，并提供CallBack接口提供方法回调，对不同query查询方法提供Callback实现。
 *       3.连接池优化：在excuteDML与excuteQuery方法中修改连接池的获取方式:createConn——>getConn
 *
 * @author cmz
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable{


    /**
     * 根据传入的DQL语句，执行sql操作
     * @param sql sql语句
     * @param clazz 查询结果的Class对象
     * @param params sql参数
     * @param callBack CallBack的实现类——>回调方法
     * @return
     */
    public List excuteQuery(String sql, Class clazz,CallBack callBack, Object... params) {
        Connection connection = DBManager.getConn();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List list = null;//返回的结果集封装在list中

        int count = 0;
        try {

            preparedStatement = connection.prepareStatement(sql);

            JDBCUtils.handleParams(preparedStatement,params);

            resultSet = preparedStatement.executeQuery();

            //方法回调
           list = callBack.doExecute(connection, preparedStatement, resultSet, clazz);
           return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(connection);
        }
        return list;
    }

    /**
     * DML模板
     *
     * 根据传入的sql参数和拼接好的sql，填充sql中的?（设置sql中的参数），并执行sql
     * @param sql    sql语句
     * @param params 参数
     * @return 执行sql成功后影响的行数（记录数）
     */
    public int excuteDML(String sql, Object[] params) {
        Connection connection = DBManager.getConn();
        int count = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //给sql设置参数，如下代码会经常使用，故封装在JDBCUtils中
           /* if (params != null) {
                for (int i = 0; i <params.length ; i++) {
                    preparedStatement.setObject(1+i,params[i]);
                }
            }*/
            JDBCUtils.handleParams(preparedStatement,params);
            count = preparedStatement.executeUpdate();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return count;
        } finally {
            DBManager.close(connection);
        }
    }

    /**
     * 根据传入的对象，拼接对象对应的插入sql
     * @param object 需要存储的对象
     * @return 插入了几条记录
     */
    public int insert(Object object) {
        //obj——>表中 insert into 表名（id,uname,pwd....） values(?,?,?,.....)
        Class c = object.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(c);
        List<Object> params = new ArrayList<Object>();//存储sql的参数对象
        StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+" (");
        int countNutField = 0;//计算不为null的属性值，方便计算sql最后应该生成几个?
        //通过反射来获取所有属性
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            //获取属性名称
            String fieldName = field.getName();
            //调用get方法获取属性值
            Object fieldValue = ReflectUtils.invokeGet(fieldName, object);
            if (fieldValue != null) {
                countNutField++;
                sql.append(fieldName + ",");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length() - 1, ')');//将最后一个多余的逗号换成)
        sql.append("values (");
        for (int i = 0; i <countNutField ; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length() - 1, ')');//将最后一个多余的逗号换成)
        int i = excuteDML(sql.toString(), params.toArray());
        return i;
    }

    /**
     * 根据传入的Class对象和主键id，拼接删除对应对象的sql
     * @param clazz 跟表对应的类的对象
     * @param id 主键id
     * @return 删除了几条记录
     */

    public int delete(Class clazz, Object id) {
        //Emp.class,2——>delete from emp where id=2;

        //通过Class对象找TableInfo
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        //根据表信息对象获取主键列对象
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " =?";
        int i = excuteDML(sql, new Object[]{id});
        return i;
    }

    /**
     * 根据传入的对象，拼接删除对应对象的sql
     * @param object 应当删除的对应数据库中的对象
     * @return 删除了几条记录
     */

    public int delete(Object object) {
        //根据输入对象object获取Class对象
        Class c = object.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(c);
        //System.out.println("tableInfo:"+tableInfo);//打桩
        //根据表信息对象获取主键列对象
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        //String sql = "delete from " + tableInfo.getTname() + " where" + onlyPriKey.getName() + " =?";

        //通过反射调用输入对象object的get方法或者set方法来获取主键id
        //注意(!!!!!!):
        // 1.这里不能用：object.get属性()方法，来获取属性，因为我们传入的对象类型为Object，所以
        //   不能通过"."来调用get方法。因为根据多态，"."只能调用Object类型的方法，不能调用Object
        //   子类的方法！！，也就是说根本点不出来——>object.getId()根本点不出来！！！
        // 2.这里也不能通过类型强转来获取id：
        //   虽然我们可以通过类型强转以及多态的性质来得到子类引用类型的对象，但是我们不知道传入的
        //   的对象类型是什么，这个信息只能通过Class对象才能得到，而想要通过Class对象得到，则必须
        //   通过反射！
        //   例如：假设我们想要将object强转为Emp，那么这个方法内就得写Emp emp = (Emp)object;
        //         但是如果我们想要删除Dept对象呢？，那只能把Emp emp = (Emp)object;改成 Dept dept = (Dept) object;
        //         一旦我们想通过调用该方法来删除所有pojo类对应的表中记录，那么就要不断的修改代码，还得动态修改！！
        //         所以这种事先不知道要传入什么对象，但是又要对任何对象都可以执行我们的方法，并且每次执行不会修改代码，
        //         我们只能通过反射!!

        //通过反射调用set和get方法很常见，所以如下代码封装在ReflectUtils中
        /*try {
            Method method = c.getMethod("get" + StringUtils.firstChar2UpperCase(onlyPriKey.getName()), null);
            Object priKeyValue = method.invoke(object, null);
            excuteDML(sql, new Object[]{priKeyValue});
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Object primaryKey = ReflectUtils.invokeGet(onlyPriKey.getName(), object);
        int i = delete(c, primaryKey);
        return i;
    }

    /**
     * 根据传入的对象和其属性名称，更新其对应数据库中的数据
     * @param object 所有要更新的对象
     * @param filedNames 更新的属性列表
     * @return 更新了几条记录
     */

    public int update(Object object, String... filedNames) {
        //obj{uname,pwd}——>update 表名 set uname=?, pwd=? where id=?
        Class c = object.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(c);
        //获取主键
        ColumnInfo priKey = tableInfo.getOnlyPriKey();

        List<Object> params = new ArrayList<Object>();//存储sql的参数对象
        StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");

        for (String fname:filedNames) {
            //通过反射调用get方法
            Object fvalue = ReflectUtils.invokeGet(fname, object);
            sql.append(fname + "=?,");
            params.add(fvalue);
        }

        //设置主键的值——>where条件的值
        params.add(ReflectUtils.invokeGet(priKey.getName(), object));

        sql.setCharAt(sql.length()-1,' ');
        sql.append(" where " + priKey.getName() + "=? ");

        return excuteDML(sql.toString(), params.toArray());
    }

    /**
     * 根据传入的对象，更新其对应数据库中的数据
     * @param object 需要更新的对象
     * @return 更新了几条记录
     */

    public int update(Object object) {
        //obj{uname,pwd}——>update 表名 set uname=?, pwd=? where id=?
        Class c = object.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(c);

        //获取主键
        ColumnInfo priKey = tableInfo.getOnlyPriKey();

        List<Object> params = new ArrayList<Object>();//存储sql的参数对象

        StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");
        int countNutField = 0;//计算不为null的属性值，方便计算sql最后应该生成几个?
        //通过反射来获取所有属性
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            //获取属性名称
            String fieldName = field.getName();

            //如果属性名称为主键，则先跳过，确保放在params集合的末尾
            if (fieldName.equalsIgnoreCase(priKey.getName())) {
                continue;
            }
            //调用get方法获取属性值
            Object fieldValue = ReflectUtils.invokeGet(fieldName, object);
            if (fieldValue != null) {
                countNutField++;
                sql.append(fieldName + "=?,");
                params.add(fieldValue);
            }
        }
        //设置主键的值——>where条件的值
        params.add(ReflectUtils.invokeGet(priKey.getName(), object));

        sql.setCharAt(sql.length() - 1, ' ');//将最后一个多余的逗号换成空格
        sql.append(" where " + priKey.getName() + "=? ");

        return excuteDML(sql.toString(),params.toArray());
    }

    /**
     * 根据传入的DQL语句，Class对象，查询条件
     * @param sql 查询语句
     * @param clazz 封装数据的javaBean类的Class对象
     * @param params sql的参数（查询条件）
     * @return 封装好的结果
     */

    public List queryRows(String sql, Class clazz, Object... params) {
        //调用excuteDQL方法
        List result = excuteQuery(sql, clazz, new CallBack() {
            @Override
            public List doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, Class clazz) {
                List list = null;//返回的结果集封装在list中
                try {
                    //获取ResultSet元信息
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    while (resultSet.next()) {

                        if (list == null) {
                            list = new ArrayList();
                        }

                        Object rowObj = clazz.newInstance();//相当于调用javaebean的无参构造器

                        //多列 select username,pwd,age from user where id>? and age>18
                        for (int i = 0; i <metaData.getColumnCount() ; i++) {//获取列数:metaData.getColumnCount()
                            String columnName = metaData.getColumnLabel(i + 1);
                            Object columnValue = resultSet.getObject(i + 1);
                    /*
                    如下方法由于方便重用，封装在了ReflectUtils中
                    Method setMethord = clazz.getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(columnName),
                            columnValue.getClass());
                    setMethord.invoke(rowObj, null);
                    */
                            ReflectUtils.invokeSet(columnName, rowObj, columnValue);
                        }
                        list.add(rowObj);

                    }
                    return list;
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } finally {
                    DBManager.close(connection);
                }
                return list;
            }
        },params);

        return result;
    }

    /**
     * 根据传入DQL执行查询sql,单行查询
     * @param sql 查询语句
     * @param clazz 封装数据的javaBean类的Class对象
     * @param params sql的参数
     * @return
     */

    public List queryUniqueRow(String sql, Class clazz, Object... params) {
        List result=excuteQuery(sql, clazz, new CallBack() {
            @Override
            public List doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, Class clazz) {
                List list = null;
                Object rowObj = null;
                try {
                    //获取ResultSet元信息
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    while (resultSet.next()) {

                        rowObj = clazz.newInstance();

                        //一行 select username,pwd,age from user where id=1 and age=20
                        for (int i = 0; i <metaData.getColumnCount() ; i++) {//获取列数:metaData.getColumnCount()
                            String columnName = metaData.getColumnLabel(i + 1);
                            Object columnValue = resultSet.getObject(i + 1);

                            ReflectUtils.invokeSet(columnName, rowObj, columnValue);
                        }


                    }
                    if (rowObj != null) {
                        list = new ArrayList();
                        list.add(rowObj);
                    }

                    return list;
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } finally {
                    DBManager.close(connection);
                }
                return list;
            }
        },params);
        return result;
    }

    /**
     * 根据sql进行单列查询
     * @param sql sql语句
     * @param clazz 需要查询的字段对应的类对象
     * @param params sql条件参数（?）
     * @return
     */
    public List queryValue(String sql, Class clazz, Object... params) {
        List result = excuteQuery(sql, clazz, new CallBack() {
            @Override
            public List doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, Class clazz) {
                List columns = null;
                try {

                    //获取ResultSet元信息
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    while (resultSet.next()) {
                        if (columns == null) {
                            columns = new ArrayList();
                        }
                        //一列 select username from user where id=1 and age=20
                        for (int i = 0; i < metaData.getColumnCount(); i++) {//获取列数:metaData.getColumnCount()
                            //String columnName = metaData.getColumnLabel(i + 1);
                            Object columnValue = resultSet.getObject(i + 1);
                            columns.add(columnValue);
                        }
                    }
                    return columns;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DBManager.close(connection);
                }
                return columns;
            }
        }, params);
        return result;
    }

    /**
     * 单列查询，返回数值
     * @param sql sql语句
     * @param clazz 字段的类的class对象
     * @param params sql条件参数
     * @return
     */
    public List<Number> queryNumber(String sql, Class clazz, Object... params) {
        return queryValue(sql,clazz,params);
    }


    /**
     * 分页查询
     * @param pageNum 第几页数据
     * @param size 每列多少记录
     * @return 查询结果的结果集
     */
    public abstract List queryPagenate(int pageNum, int size);


    /**
     * 重写clone方法
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
