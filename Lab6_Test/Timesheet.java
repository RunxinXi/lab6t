package edu.toronto.dbservice.types;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Timesheet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int lawyerId;
	public int clientId;
	public int year;
	public int month;
	public int day;
	public String description;
	public float hoursBilled;
	public boolean priority;
	
	public Timesheet() {}
	
	public Timesheet(int lawyerId, int clientId, int year, int month, int day, String description, float hoursBilled, boolean priority) {
		this.lawyerId = lawyerId;
		this.clientId = clientId;
		this.year = year;
		this.month = month;
		this.day = day;
		this.description = description;
		this.hoursBilled = hoursBilled;
		this.priority = priority;
	}
	
	public boolean getPriority() {
		return priority;
	}
	
	public static ArrayList<Timesheet> fetchList(Connection conn, int clientId, int year, int month) {
		ArrayList<Timesheet> timesheets = new ArrayList<Timesheet>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(String.format(
					"SELECT * FROM TIMESHEETS WHERE CLIENTID = %d AND YEAR = %d AND MONTH = %d",
					clientId, year, month));
			while (results.next()) {
				timesheets.add(new Timesheet(
						results.getInt("LAWYERID"),
						results.getInt("CLIENTID"),
						results.getInt("YEAR"),
						results.getInt("MONTH"),
						results.getInt("DAY"),
						results.getString("DESCRIPTION"),
						results.getFloat("HOURSBILLED"),
						results.getBoolean("PRIORITY")
				));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return timesheets;
	}
}