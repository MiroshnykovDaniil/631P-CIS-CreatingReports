package db.entity;

/**
 * Position entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Position extends Entity{
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Position [name=" + name + "]";
	}
	

}
