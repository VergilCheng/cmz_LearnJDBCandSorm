/***
 *  手写SORM框架优化：
 *  1.模板模式：采用模板方法模式对Query中的方法进行模板化，方便重用——>Query从接口变成抽象类，将例如excuteDML这种
 *              多种数据库代码一致的方法在父类中实现，子类去继承就可以。
 *
 *
 */
package sorm1;