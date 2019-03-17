package ORM.pojo;

import java.sql.Date;

/**
 * javabean
 */
public class Emp {

    private Integer id;
    private String ename;
    private Double salary;
    private Date birthday;
    private Integer age;
    private Integer deptId;

    public Emp() {
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "id=" + id +
                ", ename='" + ename + '\'' +
                ", salary=" + salary +
                ", birthday=" + birthday +
                ", age=" + age +
                ", deptId=" + deptId +
                '}';
    }
}
