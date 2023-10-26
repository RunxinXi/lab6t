package edu.toronto.dbservice.types;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Lawyer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int id;
	public String name;
	public float hourlyRate;
	
	public Lawyer() {}
	
	public Lawyer(int id, String name, float hourlyRate) {
		this.id = id;
		this.name = name;
		this.hourlyRate = hourlyRate;
	}
	
	public static Lawyer fetch(Connection conn, int id) {
		Lawyer lawyer = new Lawyer();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet lawyers = stmt.executeQuery(String.format("SELECT * FROM LAWYERS WHERE ID = %d", id));
			if (lawyers.next()) {
				lawyer.id = lawyers.getInt("ID");
				lawyer.name = lawyers.getString("NAME");
				lawyer.hourlyRate = lawyers.getFloat("HOURLYRATE");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return lawyer;
	}
}