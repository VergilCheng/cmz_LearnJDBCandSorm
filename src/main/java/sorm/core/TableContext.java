package sorm.core;

import org.junit.jupiter.api.Test;
import sorm.bean.ColumnInfo;
import sorm.bean.TableInfo;
import sorm.core.convertorImpl.MySqlTypeConvertor;
import sorm.utils.JavaFileUtils;
import sorm.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构（javaBean结构）
 *
 * 原理可以暂时不看
 */
public class TableContext {
    /**
     * 表名为key，表信息对象为value
     */
    private static Map<String,TableInfo> tables = new HashMap<String,TableInfo>();

    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    private static  Map<Class,TableInfo>  poClassTableMap = new HashMap<Class,TableInfo>();

    private TableContext(){}

    static {
        try {
            //初始化获得表的信息
            Connection con = DBManager.getConn();
            DatabaseMetaData dbmd = con.getMetaData();

            ResultSet tableRet = dbmd.getTables(null, "%","%",new String[]{"TABLE"});

            while(tableRet.next()){
                String tableName = (String) tableRet.getObject("TABLE_NAME");

                TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>()
                        ,new HashMap<String, ColumnInfo>());
                tables.put(tableName, ti);

                ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  //查询表中的所有字段
                while(set.next()){
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"), 0);
                    ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
                }

                ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  //查询t_user表中的主键
                while(set2.next()){
                    ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
                    ci2.setKeyType(1);  //设置为主键类型
                    ti.getPriKeys().add(ci2);
                }

                if(ti.getPriKeys().size()>0){  //取唯一主键。。方便使用。如果是联合主键。则为空！
                    ti.setOnlyPriKey(ti.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //调用updateJavaPojoFile方法，在每次加载类启动项目的时候根据数据库中的已经更新的表信息，更新项目中的pojo类
        updateJavaPojoFile();

        //加载pojo包下的所有类，便于重用，提高效率！！
        loadPOTables();
    }


    public static Map<String, TableInfo> getTables() {
        return tables;
    }

    public static Map<Class, TableInfo> getPoClassTableMap() {
        return poClassTableMap;
    }

    /**
     * 根据表结构，生成指定包下的所有.java文件
     *
     * 实现了从表结构转化到类结构
     */
    private static void updateJavaPojoFile() {
        Map<String, TableInfo> tables = TableContext.getTables();
        for (TableInfo tableInfo:tables.values()) {
            JavaFileUtils.createJavaPoFile(tableInfo,new MySqlTypeConvertor());
        }
    }

    /**
     * 根据pojo包中的java类来获得相应的class对象
     */
    public static void loadPOTables()  {
        for (Map.Entry<String,TableInfo> entry:tables.entrySet()) {
            String tname = entry.getKey();
            TableInfo tableInfo = entry.getValue();
            Class c = null;
            try {
                c = Class.forName(DBManager.getConf().getPoPackage() +"."+ StringUtils.firstChar2UpperCase(tname));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            poClassTableMap.put(c, tableInfo);
        }
    }

    /**
     * 测试是否能够根据TableContext得到所有表的信息
     */
    @Test
    public  void test1() {
        Map<String, TableInfo> tables = getTables();
        System.out.println(tables);
        TableInfo emp = tables.get("emp");
        Map<String, ColumnInfo> columns = emp.getColumns();
        for (ColumnInfo info : columns.values()) {
            System.out.println(info);
        }
    }

    /**
     * 测试updateJavaPojoFile方法
     */
    @Test
    public void test2() {
        TableContext.updateJavaPojoFile();
    }

}
