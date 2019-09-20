package db.entity;

/**
 * User Entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class User extends Entity{
	
	private String name;
	private String email;
	private String password;
	private long authority_id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getAuthority_id() {
		return authority_id;
	}
	public void setAuthority_id(long authority_id) {
		this.authority_id = authority_id;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", password=" + password + ", authority_id=" + authority_id
				+ "]";
	}

}
