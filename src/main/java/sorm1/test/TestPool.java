package sorm1.test;

import org.junit.jupiter.api.Test;
import sorm1.core.Query;
import sorm1.core.QueryFactory;
import sorm1.core.queryImpl.MysqlQuery;
import sorm1.pojo.Emp;

import java.sql.Date;
import java.util.List;

/**
 * 测试连接池效率
 */
public class TestPool {

    @Test
    public void testQuery() {
        Query mysqlQuery = QueryFactory.getInstance().createQuery();
        List<Emp> list = mysqlQuery.queryRows("select ename,salary,age from emp ", Emp.class, null);

        /*for (Emp emp : list) {
            System.out.println(emp.getEname());
        }*/
    }

    /**
     * 不使用连接池的测试
     */
    @Test
    public void test1() {
        long a = System.currentTimeMillis();
        int j = 0;
        for (int i = 0; i < 120; i++) {
            testQuery();
            System.out.println(++j);
        }
        long b = System.currentTimeMillis();
        System.out.println(b - a);
    }
}
