package db.entity;

import java.sql.Date;

/**
 * Reports entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Reports extends Entity{

	private long user_id;
	private Date date;
	private String name;
	private long department_id;
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}
	
	@Override
	public String toString() {
		return "Reports [user_id=" + user_id + ", date=" + date + ", name=" + name + ", department_id=" + department_id
				+ "]";
	}
	
	
}
