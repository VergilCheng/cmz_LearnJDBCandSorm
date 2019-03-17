package sorm.core;

/**
 * 负责java数据类型和数据库数据类型的相互转化
 */
public interface TypeConvertor {

    /**
     * 将数据库数据类型转化成java的数据类型
     * @param columnType 数据库字段的数据类型
     * @return Java的数据类型
     */
    String databaseType2JavaType(String columnType);

    /**
     * 负责将java数据类型转化成数据库数据类型
     * @param javaType
     * @return
     */
    String JavaType2databaseType(String javaType);

}
