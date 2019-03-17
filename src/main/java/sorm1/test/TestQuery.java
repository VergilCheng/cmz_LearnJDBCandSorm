package sorm1.test;

import org.junit.jupiter.api.Test;
import sorm1.VO.EmpVO;
import sorm1.core.queryImpl.MysqlQuery;
import sorm1.pojo.Emp;

import java.sql.Date;
import java.util.List;

public class TestQuery {
    /**
     * 测试delete方法
     *
     */
    @Test
    public void test1() {
        Emp emp = new Emp();
        emp.setId(8);
        new MysqlQuery().delete(emp);
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
        int insert = new MysqlQuery().insert(emp);
        /*
            TODO:这个删除方法暂时有一些小问题，如果对象不设置对象id属性的话，删除不会执行，因为对象中没有id的值
            TODO:但是表中维护了一个自增的主键id，所以还是要根据数据库元数据来获取id并进行删除
         */
        int delete = new MysqlQuery().delete(emp);
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
        emp.setId(9);
        emp.setAge(20);
        emp.setEname("lilith");
        emp.setBonus(12313);
        emp.setDeptId(2);
        emp.setSalary(20000.0);

        int update = new MysqlQuery().update(emp,new String[]{"age","ename","bonus","deptId","salary"});
        System.out.println(update);

        //测试update（Object obj）
        Emp emp1 = new Emp();
        emp1.setId(9);
        emp1.setAge(20);
        emp1.setEname("issac");
        emp1.setBonus(1231);
        emp1.setDeptId(2);
        emp1.setSalary(20000.0);

        int update1 = new MysqlQuery().update(emp1);
        System.out.println(update1);

    }

    /**
     * 测试queryRows方法
     */
    @Test
    public void test4() {
        //TODO:sql如果写select * 的话，会出现空指针异常，应该是*没有转换为所有的列的名称

        List<Emp> list = new MysqlQuery().queryRows("select ename,salary,age from emp where age>? and salary<?", Emp.class, new Object[]{20, 20000});
        for (Emp emp : list) {
            System.out.println(emp.getEname());
        }

        //测试联合查询：emp+dept
        String sql = "select e.id,e.ename,salary+bonus 'xinshui',age,d.dname 'deptName',d.address 'deptAddr' from emp e join dept d on e.deptId=d.id";
        List<EmpVO> list1= new MysqlQuery().queryRows(sql, EmpVO.class, null);
        for (EmpVO vo:
                list1) {
            System.out.println(vo);
        }
        //测试单行查询
        String sql1 = "select ename,salary,age from emp where ename=?";
        List<Emp> list4 = new MysqlQuery().queryUniqueRow(sql1, Emp.class, new Object[]{"程铭哲"});
        System.out.println(list4.get(0).getEname());
        //测试单列查询
        String sql2 = "select ename from emp";
        List<String> list2 = new MysqlQuery().queryValue(sql2, String.class, null);
        for (String str:
                list2) {
            System.out.println(str);
        }
        //测试单列数字查询
        String sql3 = "select salary from emp";
        List<Number> list3 = new MysqlQuery().queryNumber(sql3, Number.class, null);
        for (Number num:
                list3) {
            System.out.println(num);
        }

    }
}
