package db.entity;

/**
 * Authority entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Authority extends Entity{

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Authority [name=" + name + "]";
	}
	
}
