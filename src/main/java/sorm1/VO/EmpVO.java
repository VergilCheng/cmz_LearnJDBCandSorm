package sorm1.VO;

/**
 * Emp与Dept的连接查询封装的类
 */
public class EmpVO {
    //select e.id,e.ename,salary+bonus,age,dname,address from emp e
    //join dept d on e.deptId=d.id
    private Integer id;
    private String ename;
    private Double xinshui;
    private Integer age;
    private String deptName;
    private String deptAddr;

    public EmpVO() {
    }

    public EmpVO(Integer id, String ename, Double xinshui, Integer age, String deptName, String deptAddr) {
        this.id = id;
        this.ename = ename;
        this.xinshui = xinshui;
        this.age = age;
        this.deptName = deptName;
        this.deptAddr = deptAddr;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Double getXinshui() {
        return xinshui;
    }

    public void setXinshui(Double xinshui) {
        this.xinshui = xinshui;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddr() {
        return deptAddr;
    }

    public void setDeptAddr(String deptAddr) {
        this.deptAddr = deptAddr;
    }

    @Override
    public String toString() {
        return "EmpVO{" +
                "id=" + id +
                ", ename='" + ename + '\'' +
                ", xinshui=" + xinshui +
                ", age=" + age +
                ", deptName='" + deptName + '\'' +
                ", deptAddr='" + deptAddr + '\'' +
                '}';
    }
}
