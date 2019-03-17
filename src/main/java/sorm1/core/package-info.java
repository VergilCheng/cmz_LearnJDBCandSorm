package sorm1.core;
/**
 *  核心包：放置一些核心类
 *
 *  核心架构：
 *  1.Query接口：负责查询（对外提供服务的核心类）
 *  2.QueryFactory类：负责根据配置信心创建query对象
 *  3.TypeConvertor接口：负责类型转换
 *  4.TableContext类：负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构（javaBean结构）
 *  5.DBManager类：根据配置信息，维持连接对象的管理（增加连接池功能）
 */
