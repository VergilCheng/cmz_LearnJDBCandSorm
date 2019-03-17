package sorm1.core;

import sorm1.bean.Configuration;

/**
 * 创建Query对象的工厂方法
 *
 * 工厂类设计：
 * 1.工厂类为单例模式
 * 2.优化：克隆模式优化：如果频繁调用createQuery方法，那么会频繁使用反射去调用生成Query对象，那么效率会很慢
 *         所以我们使用克隆模式可以优化提高效率。
 */
public class QueryFactory {


    private static Class aClass;
    private static Query prototypeObj;//原型对象

    //单例模式:使用静态内部类
    private static class QueryFactoryInstance{
        private static QueryFactory instance = new QueryFactory();
    }

    public static QueryFactory getInstance() {
        return QueryFactoryInstance.instance;
    }

    private QueryFactory(){

    }

    //加载
    static {
        try {
            aClass = Class.forName(DBManager.getConf().getQueryClass());//加载指定的queryClass
            prototypeObj = (Query) aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


   /* //不使用克隆模式创建
    public Query createQuery() {
        try {
            //通过反射生成Query对象
            return (Query) aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * 返回配置文件中配置好的Query实例对象
     * @return
     */
   public Query createQuery() {
       try {
           //使用克隆模式优化创建Query对象的速度
           return (Query) prototypeObj.clone();
       } catch (CloneNotSupportedException e) {
           e.printStackTrace();
       }
       return null;
   }


}
