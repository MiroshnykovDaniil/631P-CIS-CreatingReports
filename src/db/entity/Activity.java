package db.entity;

/**
 * Activity entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Activity extends Entity{

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Activity [name=" + name + "]";
	}
	
}
