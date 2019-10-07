package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.entity.Teacher;
import db.entity.User;

/**
 * Provides methods for work with DataBase.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class DBManager {

	private static DBManager instance;
	private String url = "jdbc:postgresql://localhost:5432/postgres";
	private String login = "postgres";
	private String pass = "admin";
	
	// ======== QUERIES ==============
	
	//USER
	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE name=?";
	private static final String SQL_INSERT_USER="INSERT INTO users(name, email, password, authority_id) VALUES (?,?,?,?)";
	private static final String SQL_UPDATE_USER="UPDATE users SET name=?,email=?,password=?,authority_id=? WHERE id=?";
	private static final String SQL_DELETE_USER="DELETE FROM users WHERE name=?";
	
	private static final String SQL_GET_ALL_TEACHERS="SELECT * FROM teacher WHERE 1";
	
	//===============================

	private DBManager() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Singletone.
	 * 
	 * @return instance of this class.
	 */
	public static synchronized DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	/**
	 * Returns a DB connection from the connection pool.
	 * 
	 * @return DB connection.
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(url, login, pass);
		return con;
	}

	// ========== CLOSE METHODS ============

	private void close(Connection con, Statement st, ResultSet rs) {
		close(rs);
		close(st);
		close(con);
	}

	private void close(Connection con, Statement st) {
		close(st);
		close(con);
	}

	private void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}

	private void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	private void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
			}
		}
	}

	// ========== USER ===========
	/**
	 * Returns user found by his login.
	 * 
	 * @param login - user login.
	 * @return user entity.
	 */
	public User findUserByLogin(String login) {
		User user = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			st.setString(1, login);
			rs = st.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return user;
	}
	
	/**
	 * Inserts new row into `user` table in DB.
	 * 
	 * @param user - user to insert.
	 * @return true if new row has been added, false either.
	 */
	public boolean insertUser(User user) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_INSERT_USER);
			st.setString(1, user.getName());
			st.setString(2, user.getEmail());
			st.setString(3, user.getPassword());
			st.setLong(4, user.getAuthority_id());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}
	
	/**
	 * Updates `user` table row in DB.
	 * 
	 * @param user - user to update.
	 * @return true if row has been updated, false either.
	 */
	public boolean updateUser(User user) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_UPDATE_USER);
			st.setString(1, user.getName());
			st.setString(2, user.getEmail());
			st.setString(3, user.getPassword());
			st.setLong(4, user.getAuthority_id());
			st.setLong(5,  user.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}
	
	/**
	 * Deletes `user` table row in DB.
	 * 
	 * @param login - user login.
	 * @return true if row has been deleted, false either.
	 */
	public boolean deleteUser(String login) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_DELETE_USER);
			st.setString(1, login);
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}
	
	/**
	 * Extracts data from result set and fills user entity by it.
	 * 
	 * @param rs - ResultSet of query
	 * @return user entity
	 * @throws SQLException
	 */
	private User getUser(ResultSet rs) throws SQLException {
		User ins = new User();
		ins.setAuthority_id(rs.getLong(Fields.AUTHORITY_ID));
		ins.setEmail(rs.getString(Fields.EMAIL));
		ins.setId(rs.getLong(Fields.ID));
		ins.setName(rs.getString(Fields.NAME));
		ins.setPassword(rs.getString(Fields.PASSWORD));
		return ins;
	}
	
	//========== TEACHER =============
	
	/**
	 * Returns all teachers.
	 * 
	 * @return list of teacher entities.
	 */
	public List<Teacher> getAllTeachers(){
		List<Teacher> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_ALL_TEACHERS);
			rs = st.executeQuery();
			while (rs.next()) {
				list.add(getTeacher(rs));
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}		
		return list;
	}
	
	/**
	 * Extracts data from result set and fills teacher entity by it.
	 * 
	 * @param rs - ResultSet of query
	 * @return teacher entity
	 * @throws SQLException
	 */
	private Teacher getTeacher(ResultSet rs) throws SQLException {
		Teacher ins = new Teacher();
		ins.setId(rs.getLong(Fields.ID));
		ins.setName(rs.getString(Fields.NAME));
		ins.setDepartment_id(rs.getLong(Fields.DEPARTMENT_ID));
		ins.setPosition_id(rs.getLong(Fields.POSITION_ID));
		return ins;
	}
}
