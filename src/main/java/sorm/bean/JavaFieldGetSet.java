package sorm.bean;

/**
 * 封装了java属性和get，set方法的源码
 *
 * 作用:方便生成.java后缀的文件
 */
public class JavaFieldGetSet {
    /**
     * 属性的源码信息，如private int userId
     *
     */
    private String fieldInfo;
    /**
     * get方法的源码信息，如 public int getUserId(){....}
     */
    private String getInfo;
    /**
     * set方法的源码信息，如public void setUserId(){....}
     */
    private String setInfo;

    public JavaFieldGetSet() {
    }

    public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
        this.fieldInfo = fieldInfo;
        this.getInfo = getInfo;
        this.setInfo = setInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    @Override
    public String toString() {
        System.out.println(fieldInfo);
        System.out.println(getInfo);
        System.out.println(setInfo);
        return super.toString();
    }
}
