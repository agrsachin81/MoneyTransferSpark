package home.test.restapi.example.moneytransfer;

import static home.test.restapi.testtool.TestRekuestHelper.request;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import home.test.restapi.testtool.TestResponse;
import spark.Spark;


/**
 * To test the integration of various clients who will use this API
 * @author sachin
 *
 * Type ITMoneyTransferAPITest, created on 22-Sep-2019 at 7:18:12 pm
 *
 */
public class ITMoneyTransferAPITest {

	private static final String NAME = "name";
	private static final String BALANCE = "balance";
	private static final String ACCOUNT_ID = "accountId";
	private static final String STATUS = "status";
	private static final String MOBILE_NUMBER = "mobileNumber";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MoneyTransferAPI.main(null);
		
		Thread.sleep(5000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Spark.stop();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAccountCreate() {
		TestResponse res = request("POST", "/account","{name:john,mobileNumber:12345567908}");

		Map<String, Object> json = res.json();

		assertEquals(200, res.status);

		assertEquals("john", json.get(NAME));

		assertEquals("12345567908", json.get(MOBILE_NUMBER));
		assertEquals("mdskvndfk", "SUCCESS", json.get(STATUS));
		assertNotNull(json.get(ACCOUNT_ID));
	}
	
	@Test
	public void testAccountGet() {
		Map<String, Object> json = createAccount();
		
		TestResponse response = request("GET","/account/"+json.get(ACCOUNT_ID),"");
		System.out.println(response.body);
		json = response.json();
		assertEquals(200, response.status);
		assertEquals("Status is not correct", "SUCCESS", json.get(STATUS));
		double parseDouble = (double) json.get(BALANCE);
		assertEquals("balance should match", 900.0, parseDouble,0.00001);
	}

	AtomicInteger accCreateCounter = new AtomicInteger(0);
	private Map<String, Object> createAccount() {
		String mobileNumber ="129845567908";
		int andDecrement = accCreateCounter.getAndDecrement();
		TestResponse res = request("POST", "/account","{name:john"+andDecrement+",mobileNumber:"+mobileNumber+",balance:900}");
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);

		assertEquals("john"+andDecrement, json.get(NAME));

		assertEquals("129845567908", json.get(MOBILE_NUMBER));
		assertEquals("Status is not correct", "SUCCESS", json.get(STATUS));
		assertNotNull(json.get(ACCOUNT_ID));
		return json;
	}
	
	@Test
	public void testAccountGetNonExisting() {
		TestResponse response = request("GET","/account/PA_9876","");
		assertEquals(200, response.status);
		Map<String, Object>  json = response.json();
		assertEquals("Status is not correct", "ERROR", json.get(STATUS));
	}
	
	
	@Ignore
	@Test	
	public void testAccountDelete() {
		
	}
	
	@Ignore
	@Test	
	public void testGetAccounts() {
		
	}
	
	@Ignore
	@Test
	public void testTransactionSuccessfulAccountTransfer() {
		
	}
	
	@Ignore
	@Test
	public void testTransactionSuccessfulDebitCash() {
		
	}
	
	@Ignore
	@Test
	public void testTransactionSuccessfulCreditCash() {
		
	}
	
	@Ignore
	@Test
	public void testTransactionFailedInsufficientFunds() {
		
	}
}
