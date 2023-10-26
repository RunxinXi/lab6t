package mie.test_example;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;				// Database connection
import java.sql.SQLException;			// Exception feature for SQL database
import java.util.Arrays;
import java.util.HashMap;				// using HashMap
import java.util.List;

import org.flowable.engine.RuntimeService;		// for RuntimeService
import org.flowable.engine.TaskService;			// using service task
import org.flowable.task.api.Task;				// using user task
import org.junit.BeforeClass;					// using annotation BeforeClass for the filename of the linked BPMN diagram
import org.junit.Test;							// using annotation Test for JavaUnitTest
import org.junit.runner.RunWith;					// for @RunWith
import org.junit.runners.Parameterized;				// For @RunWith(Parameterized.class)
import org.junit.runners.Parameterized.Parameters;	// For @Parameters

import edu.toronto.dbservice.config.MIE354DBHelper;				// Handling dll file for database
import edu.toronto.dbservice.types.Invoice;

@RunWith(Parameterized.class)
public class Lab6_3_UnitTest extends LabBaseUnitTest {
	@Parameters
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
			/*
			 * { <clientId>, <year>,  <month>,   <expected total> }
			 * { 1,          2023,    4,         9999F }
			 */
		});
	}
	
	@BeforeClass
	public static void setupFile() {
		filename = "src/main/resources/diagrams/lab6_3.bpmn";
	}
	
	public int clientId;
	public int year;
	public int month;
	public float expectedTotal;
	
	public Lab6_3_UnitTest(int clientId, int year, int month, float expectedTotal) {
		this.clientId = clientId;
		this.year = year;
		this.month = month;
		this.expectedTotal = expectedTotal;
	}

	private void startProcess() {
		RuntimeService runtimeService = flowableContext.getRuntimeService();
		processInstance = runtimeService.startProcessInstanceByKey("process1");
	}

	private void submitFormData(HashMap<String, String> formEntries) {
		TaskService taskService = flowableContext.getTaskService();
		Task proposalsTask = taskService.createTaskQuery().taskDefinitionKey("usertask1").singleResult();
		flowableContext.getFormService().submitTaskFormData(proposalsTask.getId(), formEntries);
	}
	
	@Test 
	public void test() {
		Connection conn = MIE354DBHelper.getDBConnection();
		
		startProcess();
		submitFormData(new HashMap<String, String>() {{
			put("clientId", String.valueOf(clientId));
			put("month", String.valueOf(month));
			put("year", String.valueOf(year));
		}});
		
		Invoice invoice = Invoice.fetch(conn, clientId, year, month);

		assertTrue(invoice.total == expectedTotal);
		
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}