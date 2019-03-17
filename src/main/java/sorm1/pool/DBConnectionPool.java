package sorm1.pool;

import sorm1.core.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池的类
 */

public class DBConnectionPool {

    /**
     * 连接池对象
     */
    private List<Connection> pool;

    /**
     * 最大连接数
     */
    private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
    /**
     * 最小连接数
     */
    private static final int POOL_MIN_SIZE =DBManager.getConf().getPoolMinSize();


    /**
     * 初始化连接池，使池中连接数达到最小值
     */
    public void initPool() {
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }
        while (pool.size() < DBConnectionPool.POOL_MIN_SIZE) {
            pool.add(DBManager.createConn());
            System.out.println("初始化连接池，池中连接数：" + pool.size());
        }
    }

    /**
     * 构造器：在构造的连接池的时候初始化连接池
     */
    public DBConnectionPool() {
        initPool();
    }

    /**
     * 从连接池中取出一个连接：通常为list中最后一个
     * @return
     */
    public synchronized Connection getConnection() {
       /* int lastIndex = pool.size() - 1;
        if (lastIndex < 0) {
            return null;
        }
        synchronized (DBConnectionPool.class) {
            if (pool.size() - 1 <0) {
                return null;
            }
            Connection connection = pool.get(lastIndex);
            pool.remove(lastIndex);
            return connection;
        }*/
        int lastIndex = pool.size() - 1;
        Connection connection = pool.get(lastIndex);
        try {
            boolean closed = connection.isClosed();
            if (closed) {
                System.out.println("连接已经被关闭");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pool.remove(lastIndex);
        return connection;
    }

    /**
     * 将连接归还给连接池
     * @param connection 要归还或者关闭的连接
     */
    public synchronized void closeConnection(Connection connection) {
       /*if (pool.size() >= POOL_MAX_SIZE) {
           try {
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }finally {
               return;
           }
       }
        synchronized (DBConnectionPool.class) {
            if (pool.size() >= POOL_MAX_SIZE) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    return;
                }
            }
            pool.add(connection);
        }*/
        if (pool.size() == POOL_MAX_SIZE) {
            try {
                if (connection!=null) {
                    connection.close();
                    pool.remove(connection);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (connection.isClosed()) {
                    return;
                } else {
                    pool.add(connection);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




}
