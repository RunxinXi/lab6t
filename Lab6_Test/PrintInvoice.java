package mie.example;

import java.sql.Connection;
import java.util.ArrayList;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import edu.toronto.dbservice.config.MIE354DBHelper;
import edu.toronto.dbservice.types.Invoice;

public class PrintInvoice implements JavaDelegate {
	Connection conn;
	
	public PrintInvoice() {
		this.conn = MIE354DBHelper.getDBConnection();
	}
	
	@Override
	public void execute(DelegateExecution execution) {
		int clientId = Integer.parseInt((String) execution.getVariable("clientId"));
		int month = Integer.parseInt((String) execution.getVariable("month"));
		int year = Integer.parseInt((String) execution.getVariable("year"));
		Invoice invoice = Invoice.fetch(conn, clientId, year, month);
		
		System.out.println("\n\n----- START OF INVOICE -----");
		System.out.println(String.format("Month: %d / Year: %d\nClient: %d\nTotal: %.2f", invoice.month, invoice.year, clientId, invoice.total));
		System.out.println("----- END OF INVOICE   -----\n\n");
	}
}