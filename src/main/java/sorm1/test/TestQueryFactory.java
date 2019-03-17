package sorm1.test;

import org.junit.jupiter.api.Test;
import sorm1.core.Query;
import sorm1.core.QueryFactory;
import sorm1.pojo.Emp;

import java.util.List;

/**
 * 测试
 */
public class TestQueryFactory {

    /**
     * 测试工厂方法是否能够生成Query对象
     */
    @Test
    public void test1() {
        Query mysqlQuery = QueryFactory.getInstance().createQuery();
        List<Emp> list = mysqlQuery.queryRows("select ename,salary,age from emp ", Emp.class, null);
        for (Emp emp : list) {
            System.out.println(emp.getEname());
        }
    }

}
