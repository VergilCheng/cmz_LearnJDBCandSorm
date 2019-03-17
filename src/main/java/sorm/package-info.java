/***
 *  手写SORM框架：
 *
 *  核心架构：
 *  1.Query接口：负责查询（对外提供服务的核心类）
 *  2.QueryFactory类：负责根据配置信心创建query对象
 *  3.TypeConvertor接口：负责java数据类型和数据库数据类型的相互转化
 *  4.TableContext类：负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构（javaBean结构）
 *  5.DBManager类：根据配置信息，维持连接对象的管理（增加连接池功能）
 *  6.Configurraion类：根据配置文件存储配置文件的信息
 *  7.工具类：
 *      JDBCUtils：封装常用JDBC操作
 *      StringUtils：封装常用字符串操作
 *      JavaFileUtils：封装java文件操作
 *      ReflectUtils：封装常用反射操作
 *
 *
 * 针对SORM框架的说明：
 * - 核心思想：简单使用，容易上手
 * - 配置文件：目前使用资源文件、后期项目复杂可以使用XML文件
 * - 类名和表名生成：只有首字母大写有区别，其他无区别
 * - Java对象的属性由表中字段生成，完全对应
 * - 目前，只支持表中只有一个主键，联合主键不支持
 */
