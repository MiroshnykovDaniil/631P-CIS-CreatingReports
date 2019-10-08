package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.entity.*;

/**
 * Provides methods for work with DataBase.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class DBManager {

	private static DBManager instance;
	private String url = "jdbc:mysql://localhost/cis";
	private String login = "root";
	private String pass = "";
	
	// ======== QUERIES ==============
	
	//USER
	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM user WHERE name=?";
	private static final String SQL_INSERT_USER="INSERT INTO `user`(`name`, `email`, `password`, `authority_id`) VALUES (?,?,?,?)";
	private static final String SQL_UPDATE_USER="UPDATE `user` SET `name`=?,`email`=?,`password`=?,`authority_id`=? WHERE id=?";
	private static final String SQL_DELETE_USER="DELETE FROM `user` WHERE name=?";
	
	private static final String SQL_GET_ALL_TEACHERS="SELECT * FROM `teacher` WHERE 1";
	private static final String SQL_GET_TEACHER = "SELECT * FROM teacher WHERE id = ?";
	private static final String SQL_UPDATE_TEACHER = "UPDATE teacher SET `name` = ?, `department_id`=?, `position_id` = ? where `id` = ?";
	private static final String SQL_INSERT_TEACHER ="INSERT INTO teacher (`name`, `department_id`, `position_id`) VALUES (?,?,?)";
	private static final String SQL_DELETE_TEACHER ="DELETE FROM teacher where `id` = ?";


	private static final  String SQL_GET_REPORTS_BY_DEPARTMENT = "SELECT * from reports inner JOIN department on department_id = department.id where department.name = ?";
	private static final  String SQL_GET_REPORTS = "SELECT * from reports";
	private static final  String SQL_GET_REPORT = "SELECT * from report where report_id = ?";
	
    private static final String SQL_GET_ACTIVITY = "SELECT * FROM activity WHERE id = ?";
    private static final String SQL_GET_ALL_ACTIVITIES = "SELECT * FROM activity";
    private static final String SQL_UPDATE_ACTIVITY = "UPDATE activity SET `name` = ? where `id` = ?";
    private static final String SQL_INSERT_ACTIVITY ="INSERT INTO activity (`name`) VALUES (?)";
    private static final String SQL_DELETE_ACTIVITY ="DELETE FROM activity where `id` = ?";

    private static final String SQL_GET_DEPARTMENT = "SELECT * FROM department WHERE id = ?";
	private static final String SQL_GET_DEPARTMENT_BY_NAME = "SELECT * FROM department WHERE name = ?";
	private static final String SQL_GET_DEPARTMENTS ="SELECT * FROM department";
	private static final String SQL_DELETE_DEPARTMENT ="DELETE FROM department where `id` = ?";
	private static final String SQL_UPDATE_DEPARTMENT ="UPDATE department SET `name` = ? where `id` = ?";
	private static final String SQL_INSERT_DEPARTMENT ="INSERT INTO department (`name`) VALUES (?)";


	private static final String SQL_GET_REPORT_BY_ID = "SELECT * FROM reports WHERE id=?";
	private static final String SQL_INSERT_REPORTS = "INSERT INTO `reports` (`user_id`, `date`, `name`, `department_id`) VALUES (?,?,?,?)";
	private static final String SQL_UPDATE_REPORTS = "UPDATE `reports` SET `user_id`=?, `date`=?, `name`=?, `department_id` = ? WHERE id = ?";
	private static final String SQL_GET_REPORT_ID = "SELECT * FROM reports WHERE `name` = ?";

	private static final String SQL_DELETE_REPORTS = "DELETE FROM reports where `id` = ?";

	private static final String SQL_GET_POSITION = "SELECT * FROM position";
	private static final String SQL_UPDATE_POSITION = "UPDATE position SET `name` = ? where `id` = ?";
	private static final String SQL_INSERT_POSITION ="INSERT INTO position (`name`) VALUES (?)";
	private static final String SQL_DELETE_POSITION ="DELETE FROM position where `id` = ?";
	private static final String SQL_GET_POSITION_BY_ID ="SELECT * FROM position WHERE id = ?";

	//===============================

	private DBManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
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

	public Reports findReportById(long id) {
		Reports reports = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_REPORT_BY_ID);
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				reports = getReports(rs);
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return reports;
	}

	public long findReportId(String name) {
		Reports reports = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_REPORT_ID);
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next()) {
				reports = getReports(rs);
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return reports.getId();
	}

	public Position getPositionById(long id) {
		Position position = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_POSITION_BY_ID);
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				position = getPosition(rs);
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return position;
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

	public boolean insertReports(Reports reports){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_INSERT_REPORTS);
			st.setLong(1, reports.getUser_id());
			st.setDate(2, reports.getDate());
			st.setLong(4, reports.getDepartment_id());
			st.setString(3, reports.getName());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

	public boolean insertDepartment(String department){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_INSERT_DEPARTMENT);
			st.setString(1, department);
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

	public boolean insertPosition(String position){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_INSERT_POSITION);
			st.setString(1, position);
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

    public boolean insertActivity(String activity){
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQL_INSERT_ACTIVITY);
            st.setString(1, activity);
            st.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
        } finally {
            close(con, st);
        }
        return true;
    }

	public boolean insertTeacher(Teacher teacher){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_INSERT_TEACHER);
			st.setString(1, teacher.getName());
			st.setLong(2, teacher.getDepartment_id());
			st.setLong(3, teacher.getPosition_id());
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

	public boolean updateReports(Reports reports){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_UPDATE_REPORTS);
			st.setLong(1, reports.getUser_id());
			st.setDate(2, reports.getDate());
			st.setString(3, reports.getName());
			st.setLong(4, reports.getDepartment_id());
			st.setLong(5, reports.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

	public boolean updateDepartment(String newName,Department department){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_UPDATE_DEPARTMENT);
			st.setString(1, newName);
			st.setLong(2, department.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

	public boolean updatePosition(String newName, Position position){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_UPDATE_POSITION);
			st.setString(1, newName);
			st.setLong(2, position.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

    public boolean updateActivity(String newName, Activity activity){
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQL_UPDATE_ACTIVITY);
            st.setString(1, newName);
            st.setLong(2, activity.getId());
            st.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
        } finally {
            close(con, st);
        }
        return true;
    }

	public boolean updateTeacher(Teacher teacher){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_UPDATE_TEACHER);
			st.setString(1, teacher.getName());
			st.setLong(2, teacher.getDepartment_id());
			st.setLong(3, teacher.getPosition_id());
			st.setLong(2, teacher.getId());
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

	public boolean deleteReports(Reports reports){
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQL_DELETE_REPORTS);
            st.setLong(1, reports.getId());
            st.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
        } finally {
            close(con, st);
        }
        return true;
    }

	public boolean deleteDepartment(Department department){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_DELETE_DEPARTMENT);
			st.setLong(1, department.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

	public boolean deletePosition(Position position){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_DELETE_POSITION);
			st.setLong(1, position.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st);
		}
		return true;
	}

    public boolean deleteActivity(Activity activity){
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQL_DELETE_ACTIVITY);
            st.setLong(1, activity.getId());
            st.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
        } finally {
            close(con, st);
        }
        return true;
    }

	public boolean deleteTeacher(Teacher teacher){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConnection();
		st = con.prepareStatement(SQL_DELETE_TEACHER);
			st.setLong(1, teacher.getId());
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

	public List<Reports> getReportsByDepartment(int department){
		List<Reports> reports = new ArrayList<>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_REPORTS_BY_DEPARTMENT);
			st.setInt(1,department);
			rs = st.executeQuery();
			while (rs.next()) {
				reports.add(getReports(rs));
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return reports;
	}

	public List<Reports> getAllReports() {
		List<Reports> reports = new ArrayList<>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_REPORTS);
			rs = st.executeQuery();
			while (rs.next()) {
				reports.add(getReports(rs));
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return reports;
	}

	public List<Report> getReportById(int id){
		//SQL_GET_REPORT
		List<Report> report = new ArrayList<>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_REPORT);
			st.setInt(1,id);
			rs = st.executeQuery();
			while (rs.next()) {
				report.add(getReport(rs));
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return report;
	}
	
	public Activity getActivityById(long id) {
        Activity activity = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQL_GET_ACTIVITY);
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                activity = getActivity(rs);
            }
            con.commit();
        } catch (SQLException e) {
            rollback(con);
        } finally {
            close(con, st, rs);
        }
        return activity;
    }
	
	public Department getDepartmentById(long id) {
		Department department = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_DEPARTMENT);
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				department = getDepartment(rs);
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return department;
	}

	public Department getDepartmentByName(String name) {
		Department department = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_DEPARTMENT_BY_NAME);
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next()) {
				department = getDepartment(rs);
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return department;
	}

	public List<Department> getDepartment() {
		List<Department> department = new ArrayList<>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_DEPARTMENTS);
			rs = st.executeQuery();
			while (rs.next()) {
				department.add(getDepartment(rs));
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return department;
	}

	public List<Position> getPosition() {
		List<Position> position = new ArrayList<>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.prepareStatement(SQL_GET_POSITION);
			rs = st.executeQuery();
			while (rs.next()) {
				position.add(getPosition(rs));
			}
			con.commit();
		} catch (SQLException e) {
			rollback(con);
		} finally {
			close(con, st, rs);
		}
		return position;
	}

    public List<Activity> getActivity() {
        List<Activity> activity = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQL_GET_ALL_ACTIVITIES);
            rs = st.executeQuery();
            while (rs.next()) {
                activity.add(getActivity(rs));
            }
            con.commit();
        } catch (SQLException e) {
            rollback(con);
        } finally {
            close(con, st, rs);
        }
        return activity;
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

	private Reports getReports(ResultSet rs) throws  SQLException{
		Reports reports = new Reports();
		reports.setId(rs.getLong(Fields.ID));
		reports.setName(rs.getString(Fields.NAME));
		reports.setDepartment_id(rs.getLong(Fields.DEPARTMENT_ID));
		reports.setDate(rs.getDate(Fields.DATE));
		return reports;
	}

	private Report getReport(ResultSet rs) throws  SQLException{
		Report report = new Report();
		report.setId(rs.getLong(Fields.ID));
		report.setStatus(rs.getString(Fields.STATUS));
		report.setDate(rs.getDate(Fields.DATE));
		report.setReport_id(rs.getLong(Fields.REPORT_ID));
		report.setActivity_id(rs.getLong(Fields.ACTIVITY_ID));
		report.setUser_id(rs.getLong(Fields.USER_ID));

		return report;
	}
    
    private Activity getActivity(ResultSet rs) throws SQLException {
        Activity ins = new Activity();
        ins.setId(rs.getLong(Fields.ID));
        ins.setName(rs.getString(Fields.NAME));
        return ins;
    }
    
    private Department getDepartment(ResultSet rs) throws SQLException {
        Department ins = new Department();
        ins.setId(rs.getLong(Fields.ID));
        ins.setName(rs.getString(Fields.NAME));
        return ins;
    }

	private Position getPosition(ResultSet rs) throws SQLException {
		Position ins = new Position();
		ins.setId(rs.getLong(Fields.ID));
		ins.setName(rs.getString(Fields.NAME));
		return ins;
	}
}