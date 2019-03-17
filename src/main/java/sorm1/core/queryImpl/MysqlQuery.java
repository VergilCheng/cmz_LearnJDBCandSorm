package sorm1.core.queryImpl;


import sorm1.core.Query;


import java.util.List;

/**
 * 负责针对Mysql数据库的查询
 *
 * DQL与DML方法都移动到了父类Query中
 */
@SuppressWarnings("all")
public class MysqlQuery extends Query {


    /**
     * mysql的分页查询
     * @param pageNum 第几页数据
     * @param size 每列多少记录
     * @return
     */
    @Override
    public List queryPagenate(int pageNum, int size) {
        return null;
    }
}
