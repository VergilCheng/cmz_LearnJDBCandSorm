package sorm1.pojo;

import java.sql.*;
import java.util.*;

public class Emp {

	private java.sql.Date birthday;
	private String ename;
	private String gender;
	private Integer bonus;
	private Integer deptId;
	private Integer id;
	private Double salary;
	private Integer age;



	public java.sql.Date getBirthday(){
		return birthday;
	}
	public String getEname(){
		return ename;
	}
	public String getGender(){
		return gender;
	}
	public Integer getBonus(){
		return bonus;
	}
	public Integer getDeptId(){
		return deptId;
	}
	public Integer getId(){
		return id;
	}
	public Double getSalary(){
		return salary;
	}
	public Integer getAge(){
		return age;
	}
	public void setBirthday(java.sql.Date birthday){
		this.birthday=birthday;
	}
	public void setEname(String ename){
		this.ename=ename;
	}
	public void setGender(String gender){
		this.gender=gender;
	}
	public void setBonus(Integer bonus){
		this.bonus=bonus;
	}
	public void setDeptId(Integer deptId){
		this.deptId=deptId;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setSalary(Double salary){
		this.salary=salary;
	}
	public void setAge(Integer age){
		this.age=age;
	}


}
