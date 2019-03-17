package sorm1.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了反射常用操作
 */
@SuppressWarnings("all")
public class ReflectUtils {

    /**
     * 调用object对象对应属性fiedName的get方法
     * @param fieldName 属性名称
     * @param object 拥有get方法的对象
     * @return
     */
    public static Object invokeGet(String fieldName,Object object) {
        try {
            Class c = object.getClass();
            Method method = c.getMethod("get" + StringUtils.firstChar2UpperCase(fieldName), null);
            Object value = method.invoke(object, null);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过反射调用object对象对应属性fiedName的get方法
     * @param fieldName 属性名称
     * @param object 拥有get方法的对象
     * @param columnValue 根据数据库元数据查询得到的当前表中字段的数据
     */
    public static void invokeSet(String fieldName, Object object,Object columnValue) {
        if (columnValue != null) {//防止数据库中字段值为null而出现空指针异常
            Class clazz = object.getClass();
            try {
                //获取set方法
                Method setMethod = clazz.getDeclaredMethod("set" + StringUtils.firstChar2UpperCase(fieldName),
                        columnValue.getClass());//通过getClass()方法获得参数的类别
                //调用set方法
                setMethod.invoke(object, columnValue);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }
}
