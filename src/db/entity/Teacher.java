package db.entity;

/**
 * Teacher entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Teacher extends Entity{
	
	private long position_id;
	private long department_id;
	private String name;
	
	public long getPosition_id() {
		return position_id;
	}
	public void setPosition_id(long position_id) {
		this.position_id = position_id;
	}
	public long getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Teacher [position_id=" + position_id + ", department_id=" + department_id + ", name=" + name + "]";
	}
	

}
