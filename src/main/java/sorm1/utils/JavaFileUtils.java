package sorm1.utils;

import org.junit.jupiter.api.Test;
import sorm1.bean.ColumnInfo;
import sorm1.bean.JavaFieldGetSet;
import sorm1.bean.TableInfo;
import sorm1.core.DBManager;
import sorm1.core.TableContext;
import sorm1.core.TypeConvertor;
import sorm1.core.convertorImpl.MySqlTypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了生成Java文件常用操作
 */
public class JavaFileUtils {

    /**
     * 根据封装好的字段信息和类型转换器，将字段信息生成相应的javaBean.java文件，其中包括了字段的get和set方法以及属性
     *
     * 如：varchar username——>private String username;以及属性相应的get和set方法源码
     * @param column 字段信息javabean
     * @param convertor 类型转换器，根据不同数据库做数据库数据类型——>java数据类型的转换
     * @return 封装好的JavaFiledGetSet对象，再根据该对象生成相应的.java后缀文件
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor) {
        JavaFieldGetSet javaFieldGetSet = new JavaFieldGetSet();
        //获取数据类型
        String javaFieldType = convertor.databaseType2JavaType(column.getDataType());

        //拼接属性
        javaFieldGetSet.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");

        //拼接get方法
        //public java类型 get字段名(){return 字段名;}
        StringBuilder getBuilder = new StringBuilder();
        getBuilder.append("\tpublic "+javaFieldType+" get"+StringUtils.firstChar2UpperCase(column.getName())+"(){\n");
        getBuilder.append("\t\treturn " + column.getName()+";\n");
        getBuilder.append("\t}\n");
        javaFieldGetSet.setGetInfo(getBuilder.toString());

        //拼接set方法
        //public void set字段名(java类型 字段名){this.字段名=字段名}
        StringBuilder setBuilder = new StringBuilder();
        setBuilder.append("\tpublic void set" + StringUtils.firstChar2UpperCase(column.getName()) + "(");
        setBuilder.append(javaFieldType + " " + column.getName()+"){\n");
        setBuilder.append("\t\tthis." + column.getName() + "=" + column.getName() + ";\n");
        setBuilder.append("\t}\n");
        javaFieldGetSet.setSetInfo(setBuilder.toString());

        return javaFieldGetSet;
    }


    /**
     * 根据表信息，生成.java文件的所有代码
     *
     * 如 import，public class 类名{}等等
     * @param tableInfo 表信息
     * @param convertor 类型转换器
     * @return java文件代码的字符
     */
    public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor) {
        StringBuilder src = new StringBuilder();
        //取出所有的表的字段
        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        //缓存
        List<JavaFieldGetSet> javaFieldGetSetList = new ArrayList<JavaFieldGetSet>();
        for (ColumnInfo info: columns.values()) {
            javaFieldGetSetList.add(createFieldGetSetSRC(info, convertor));
        }
        //拼接.java文件
        //1.package语句
        src.append("package " + DBManager.getConf().getPoPackage() + ";\n\n");
        //2.import语句
        src.append("import java.sql.*;\n").append("import java.util.*;\n\n");
        //3.类声明语句
        src.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + " {\n\n");
        //4.属性列表
        for (JavaFieldGetSet javaFieldGetSet : javaFieldGetSetList) {
            src.append(javaFieldGetSet.getFieldInfo());
        }
        src.append("\n\n\n");
        //5.get方法列表
        for (JavaFieldGetSet javaFieldGetSet : javaFieldGetSetList) {
            src.append(javaFieldGetSet.getGetInfo());
        }
        //6.set方法列表
        for (JavaFieldGetSet javaFieldGetSet : javaFieldGetSetList) {
            src.append(javaFieldGetSet.getSetInfo());
        }
        //7.生成类结束
        src.append("\n\n}\n");
        //System.out.println(src);//打桩
        return src.toString();
    }


    /**
     * 根据.java文件字符串生成.java文件
     */
    public static void createJavaPoFile(TableInfo tableInfo, TypeConvertor convertor) {
        //Java文件的字符串
        String src = createJavaSrc(tableInfo, convertor);
        //得到src的路径
        String srcPath = DBManager.getConf().getSrcPath() + "/";
        //src的包路径中的'.'替换为'/'
        String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");

        //创建pojo包的路径
        File file = new File(srcPath + packagePath );

        //System.out.println("打桩："+file.getAbsolutePath());//打桩

        if (!file.exists()) {//如果注定目录不存在，则创建该目录
            file.mkdirs();
        }


        BufferedWriter bufferedWriter = null;
        try {
            //创建类文件
            bufferedWriter = new BufferedWriter(
                                new FileWriter(file.getAbsoluteFile()+ "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java"));
            bufferedWriter.write(src);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     *
     * 根据TableContext中的所有表信息，在指定包中生成所有的pojo类的.java文件
     *
     * 注意：这个方法应该放在TableContext中
     *
     * @param convertor 类型转换器
     */
    public static void createAllJavaPoFile(TypeConvertor convertor) {
        Map<String, TableInfo> tables = TableContext.getTables();
        for (TableInfo tableInfo:tables.values()) {
            createJavaPoFile(tableInfo,convertor);
        }
    }



    /**
     * 测试是否能够根据封装好的字段对象生成相应的java属性以及set和get方法的字符串
     */
    @Test
    public  void test1() {
        ColumnInfo columnInfo = new ColumnInfo("username", "char", 0);
        JavaFieldGetSet fieldGetSetSRC = createFieldGetSetSRC(columnInfo, new MySqlTypeConvertor());
        System.out.println(fieldGetSetSRC);
    }
    /**
     * 测试是否能够根据封装好的表对象生成相应的.java文件的字符串
     */
    @Test
    public void test2() {
        Map<String, TableInfo> tables = TableContext.getTables();
        TableInfo info = tables.get("emp");
        createJavaSrc(info, new MySqlTypeConvertor());

    }

    /**
     * 测试是否能够根据封装好的.java文件字符串生成.java文件
     */
    @Test
    public void test3() {
        Map<String, TableInfo> tables = TableContext.getTables();
        TableInfo info = tables.get("emp");
        createJavaPoFile(info, new MySqlTypeConvertor());
    }

    /**
     * 测试是否能够根据封装好的TableContext生成在pojo包下生成所有的.java文件
     */
    @Test
    public void test4() {
        createAllJavaPoFile(new MySqlTypeConvertor());
    }
}
