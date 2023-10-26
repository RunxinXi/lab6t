package edu.toronto.dbservice.types;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int clientId;
	public int year;
	public int month;
	public float total;
	
	public Invoice(int clientId, int year, int month, float total) {
		this.clientId = clientId;
		this.year = year;
		this.month = month;
		this.total = total;
	}
	
	public static Invoice fetch(Connection conn, int clientId, int year, int month) {
		try {
			ResultSet results = conn.createStatement().executeQuery(String.format(
					"SELECT * FROM INVOICES WHERE CLIENTID = %d AND YEAR = %d AND MONTH = %d",
					clientId, year, month));
			if (results.next()) {
				return new Invoice(
						results.getInt("clientId"),
						results.getInt("year"),
						results.getInt("month"),
						results.getFloat("total")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insert(Connection conn) {
		try {
			conn.createStatement().execute(String.format(
					"INSERT INTO invoices(clientId, year, month, total) VALUES(%d, %d, %d, %f)",
					clientId, year, month, total));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Connection conn) {
		try {
			conn.createStatement().execute(String.format(
					"UPDATE INVOICES SET TOTAL = %f WHERE CLIENTID = %d AND YEAR = %d AND MONTH = %d",
					total, clientId, year, month));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addToTotal(Connection conn, int clientId, int year, int month, float total) {
		float roundedTotal = Math.round(total * 100F) / 100F;
		Invoice invoice = fetch(conn, clientId, year, month);
		if (invoice == null) {
			(new Invoice(clientId, year, month, roundedTotal)).insert(conn);
			return;
		}
		invoice.total += roundedTotal;
		invoice.update(conn);
	}
}