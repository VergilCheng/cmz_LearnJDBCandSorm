package sorm.core.convertorImpl;

import sorm.core.TypeConvertor;

/**
 * mysql数据库的数据类型和java数据类型的转换
 */
public class MySqlTypeConvertor implements TypeConvertor {


    /**
     * 将数据库类型转化成java的数据类型
     * @param columnType 数据库字段的数据类型
     * @return
     */
    public String databaseType2JavaType(String columnType) {


        //varchar--->String,等等类型转换
        if ("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)) {
            return "String";
        } else if ("int".equalsIgnoreCase(columnType)
                ||"tinyint".equalsIgnoreCase(columnType)
                ||"smallint".equalsIgnoreCase(columnType)
                ||"integer".equalsIgnoreCase(columnType)
                ) {
            return "Integer";
        } else if ("bigint".equalsIgnoreCase(columnType)) {
            return "Long";
        } else if ("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)) {
            return "Double";
        } else if ("clob".equalsIgnoreCase(columnType)) {
            return "java.sql.Clob";
        } else if ("blob".equalsIgnoreCase(columnType)) {
            return "java.sql.Bolb";
        } else if ("time".equalsIgnoreCase(columnType)) {
            return "java.sql.Time";
        } else if ("timestamp".equalsIgnoreCase(columnType)) {
            return "java.sql.Timestamp";
        } else if ("date".equalsIgnoreCase(columnType)) {
            return "java.sql.Date";
        }
        return null;
    }


    /**
     * 将java数据类型转化成数据库数据类型
     * @param javaType java数据类型
     * @return 数据库数据类型
     */
    public String JavaType2databaseType(String javaType) {
        return null;
    }
}
