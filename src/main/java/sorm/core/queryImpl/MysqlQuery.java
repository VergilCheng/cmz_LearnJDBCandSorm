package sorm.core.queryImpl;

import org.junit.jupiter.api.Test;
import sorm.VO.EmpVO;
import sorm.bean.ColumnInfo;
import sorm.bean.TableInfo;
import sorm.core.DBManager;
import sorm.core.Query;
import sorm.core.TableContext;
import sorm.pojo.Emp;
import sorm.utils.JDBCUtils;
import sorm.utils.ReflectUtils;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责针对Mysql数据库的查询
 */
public class MysqlQuery implements Query {

    /**
     * 测试delete方法
     *
     */
    @Test
    public void test1() {
        Emp emp = new Emp();
        emp.setId(6);
        delete(emp);
    }

    /**
     * 测试insert方法
     *
     */
    @Test
    public void test2() {
        Emp emp = new Emp();
        emp.setAge(20);
        emp.setEname("李佳起");
        emp.setBonus(123123);
        emp.setDeptId(1);
        emp.setSalary(12000.0);
        emp.setBirthday(new Date(System.currentTimeMillis()));
        int insert = insert(emp);
        /*
            TODO:这个删除方法暂时有一些小问题，如果对象不设置对象id属性的话，删除不会执行，因为对象中没有id的值
            TODO:但是表中维护了一个自增的主键id，所以还是要根据数据库元数据来获取id并进行删除
         */
        int delete = delete(emp);
        System.out.println(insert);
        System.out.println(delete);
    }

    /**
     * 测试update方法
     *
     */
    @Test
    public void test3() {
        //测试update（Object，String[]）
        Emp emp = new Emp();
        emp.setId(7);
        emp.setAge(20);
        emp.setEname("lilith");
        emp.setBonus(12313);
        emp.setDeptId(2);
        emp.setSalary(20000.0);

        int update = update(emp,new String[]{"age","ename","bonus","deptId","salary"});
        System.out.println(update);

        //测试update（Object obj）
        Emp emp1 = new Emp();
        emp1.setId(7);
        emp1.setAge(20);
        emp1.setEname("issac");
        emp1.setBonus(1231);
        emp1.setDeptId(2);
        emp1.setSalary(20000.0);

        int update1 = update(emp1);
        System.out.println(update1);

    }

    /**
     * 测试queryRows方法
     */
    @Test
    public void test4() {
        //TODO:sql如果写select * 的话，会出现空指针异常，应该是*没有转换为所有的列的名称

        List<Emp> list = queryRows("select ename,salary,age from emp where age>? and salary<?", Emp.class, new Object[]{20, 20000});
        for (Emp emp : list) {
            System.out.println(emp.getEname());
        }

        //测试联合查询：emp+dept
        String sql = "select e.id,e.ename,salary+bonus 'xinshui',age,d.dname 'deptName',d.address 'deptAddr' from emp e join dept d on e.deptId=d.id";
        List<EmpVO> list1= queryRows(sql, EmpVO.class, null);
        for (EmpVO vo:
             list1) {
            System.out.println(vo);
        }
        //测试单行查询
        String sql1 = "select ename,salary,age from emp where ename=?";
        Emp emp = (Emp) queryUniqueRow(sql1, Emp.class, new Object[]{"程铭哲"});
        System.out.println(emp.getEname());
        //测试单列查询
        String sql2 = "select ename from emp";
        List<String> list2 = queryValue(sql2, Emp.class, null);
        for (String str:
                list2) {
            System.out.println(str);
        }
        //测试单列数字查询
        String sql3 = "select salary from emp";
        List<Number> list3 = queryNumber(sql3, Emp.class, null);
        for (Number num:
                list3) {
            System.out.println(num);
        }

    }

    /**
     * 根据传入的sql参数和拼接好的sql，填充sql中的?（设置sql中的参数），并执行sql
     * @param sql    sql语句
     * @param params 参数
     * @return 执行sql成功后影响的行数（记录数）
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
    public int delete(Object object) {
        //根据输入对象object获取Class对象
        Class c = object.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(c);
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
    @Override
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
    @Override
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
    @Override
    public List queryRows(String sql, Class clazz, Object... params) {
        Connection connection = DBManager.getConn();

        List list = null;//返回的结果集封装在list中

        int count = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            JDBCUtils.handleParams(preparedStatement,params);

            ResultSet resultSet = preparedStatement.executeQuery();

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

    /**
     * 根据传入DQL执行查询sql
     * @param sql 查询语句
     * @param clazz 封装数据的javaBean类的Class对象
     * @param params sql的参数
     * @return
     */
    @Override
    public Object queryUniqueRow(String sql, Class clazz, Object... params) {
        Connection connection = DBManager.getConn();

        Object rowObj = null;
        int count = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            JDBCUtils.handleParams(preparedStatement,params);

            ResultSet resultSet = preparedStatement.executeQuery();

            //获取ResultSet元信息
            ResultSetMetaData metaData = resultSet.getMetaData();

            while (resultSet.next()) {

                rowObj = clazz.newInstance();

                //一列 select username,pwd,age from user where id=1 and age=20
                for (int i = 0; i <metaData.getColumnCount() ; i++) {//获取列数:metaData.getColumnCount()
                    String columnName = metaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);

                    ReflectUtils.invokeSet(columnName, rowObj, columnValue);
                }


            }
            return rowObj;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            DBManager.close(connection);
        }
        return rowObj;
    }

    @Override
    public List queryValue(String sql, Class clazz, Object... params) {
        Connection connection = DBManager.getConn();

        List columns = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            JDBCUtils.handleParams(preparedStatement,params);

            ResultSet resultSet = preparedStatement.executeQuery();

            //获取ResultSet元信息
            ResultSetMetaData metaData = resultSet.getMetaData();

            while (resultSet.next()) {
                if (columns == null) {
                    columns = new ArrayList();
                }
                //一列 select username,pwd,age from user where id=1 and age=20
                for (int i = 0; i <metaData.getColumnCount() ; i++) {//获取列数:metaData.getColumnCount()
                    //String columnName = metaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);
                    columns.add(columnValue);
                }


            }
            return columns;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBManager.close(connection);
        }
        return columns;
    }

    @Override
    public List<Number> queryNumber(String sql, Class clazz, Object... params) {
        return queryValue(sql,clazz,params);
    }
}
