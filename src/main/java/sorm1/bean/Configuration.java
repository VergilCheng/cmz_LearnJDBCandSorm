package sorm1.bean;

/**
 * 管理配置文件的信息
 *
 * 优化：在db1.properties中添加了queryClass字段，用来告诉框架应该让QueryFactory生成什么Query的实现类
 */
public class Configuration {

    /**
     * 驱动类
     */
    private String driver;
    /**
     * jdbc的url
     */
    private String url;
    /**
     * 数据库的用户名
     */
    private String user;
    /**
     * 数据库密码
     */
    private String pwd;
    /**
     * 正在使用哪个数据库
     */
    private String usingDB;
    /**
     * 项目的源码路径
     */
    private String srcPath;
    /**
     * 扫描生成的pojo类的包
     */
    private String poPackage;
    /**
     * 扫描QueryFactory应该生成的Query对象
     */
    private String queryClass;
    /**
     * 连接池最小连接数
     */
    private int poolMinSize;
    /**
     * 连接池最大连接数
     */
    private int poolMaxSize;

    public Configuration() {
    }

    public Configuration(String driver, String url, String user, String pwd, String usingDB, String srcPath, String poPackage) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.usingDB = usingDB;
        this.srcPath = srcPath;
        this.poPackage = poPackage;
    }


    public int getPoolMinSize() {
        return poolMinSize;
    }

    public void setPoolMinSize(int poolMinSize) {
        this.poolMinSize = poolMinSize;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", pwd='" + pwd + '\'' +
                ", usingDB='" + usingDB + '\'' +
                ", srcPath='" + srcPath + '\'' +
                ", poPackage='" + poPackage + '\'' +
                ", queryClass='" + queryClass + '\'' +
                '}';
    }
}
