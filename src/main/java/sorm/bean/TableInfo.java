package sorm.bean;

import java.util.*;

/**
 * 存储表结构的信息
 */
public class TableInfo {

    /**
     * 表名
     */
    private String tname;

    /**
     * 所有字段信息
     */
    private Map<String, ColumnInfo> columns;

    /**
     * 唯一主键（目前我们只能处理表中只有一个主键的情况）
     */
    private ColumnInfo onlyPriKey;

    /**
     * 联合主键，存储在这里，为以后使用
     */
    private List<ColumnInfo> priKeys;


    public TableInfo() {
    }




    public TableInfo(String tname, Map<String, ColumnInfo> columns, ColumnInfo onlyPriKey) {
        this.tname = tname;
        this.columns = columns;
        this.onlyPriKey = onlyPriKey;
    }

    public TableInfo(String tname, List<ColumnInfo> priKeys, Map<String, ColumnInfo> columns) {
        this.tname = tname;
        this.priKeys = priKeys;
        this.columns = columns;
    }


    public List<ColumnInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColumnInfo> priKeys) {
        this.priKeys = priKeys;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Map<String, ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnInfo> columns) {
        this.columns = columns;
    }

    public ColumnInfo getOnlyPriKey() {
        return onlyPriKey;
    }

    public void setOnlyPriKey(ColumnInfo onlyPriKey) {
        this.onlyPriKey = onlyPriKey;
    }


    @Override
    public String toString() {
        return "TableInfo{" +
                "tname='" + tname + '\'' +
                ", columns=" + columns +
                ", onlyPriKey=" + onlyPriKey +
                ", priKeys=" + priKeys +
                '}';
    }
}
