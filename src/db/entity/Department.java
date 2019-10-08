package db.entity;

/**
 * Department entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Department extends Entity{

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
