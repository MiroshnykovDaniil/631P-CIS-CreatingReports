package db.entity;

/**
 * Abstract entity. Ancestor of all entities.
 * 
 * @author Ilya Kruhlenko
 *
 */
public abstract class Entity{
	
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
