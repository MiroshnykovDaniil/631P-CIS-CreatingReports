package db.entity;

import java.sql.Date;

/**
 * Report entity.
 * 
 * @author Ilya Kruhlenko
 *
 */
public class Report extends Entity{
	
	private long report_id;
	private long user_id;
	private long activity_id;
	private Date date;
	private String status;
	public long getReport_id() {
		return report_id;
	}
	public void setReport_id(long report_id) {
		this.report_id = report_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(long activity_id) {
		this.activity_id = activity_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Report [report_id=" + report_id + ", user_id=" + user_id + ", activity_id=" + activity_id + ", date="
				+ date + ", status=" + status + "]";
	}
	

}
