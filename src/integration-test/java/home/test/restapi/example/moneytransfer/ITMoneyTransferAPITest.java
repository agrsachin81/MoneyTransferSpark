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
 * 
 * @author sachin
 *
 *         Type ITMoneyTransferAPITest, created on 22-Sep-2019 at 7:18:12 pm
 *
 */
public class ITMoneyTransferAPITest {

	private static final String NAME_PREFIX = "john";
	private static final String NAME = "name";
	private static final String BALANCE = "balance";
	private static final String ACCOUNT_ID = "accountId";
	private static final String STATUS = "status";
	private static final String MOBILE_NUMBER = "mobileNumber";
	private static final String TRAN_REK_ID_PREFIX = "TRANS_";

	private static final int MOBILE_NUMBER_START = 1000000;

	private static final double INIT_BALANCE = 300;

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
		Map<String, Object> json = createAccountRetJson();
		verifyAccountCreation(json);
	}

	@Test
	public void testAccountGet() {
		String accId = createAccountReturnAccountId();

		TestResponse response = request("GET", "/account/" + accId, "");
		System.out.println(response.body);
		Map<String, Object> json = response.json();
		assertEquals(200, response.status);
		verifyAccountCreation(json);
	}

	@Test
	public void testAccountGetNonExisting() {
		TestResponse response = request("GET", "/account/PA_9876", "");
		assertEquals(200, response.status);
		Map<String, Object> json = response.json();
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

	@Test
	public void testTransactionSuccessfulAccountTransfer() {
		String accountId = createAccountReturnAccountId();
		String accountId2 = createAccountReturnAccountId();
		
		Map<String, Object> json = transferAndRetJson(accountId, 100, accountId2);
		
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
	
	private Map<String,Object> transferAndRetJson(String origAccountId, double amount, String cpAccountId) {
		int counter = transactionCreateCounter.incrementAndGet();
		
		TestResponse res = request("POST", "/account/"+origAccountId+"/transact", "{amount:" + amount + ",cpAccountId:"+cpAccountId
				+ ",transactionRekuestId:" +TRAN_REK_ID_PREFIX+ counter + "}");
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);
		return json;
	}

	private Map<String, Object> createAccountRetJson() {
		int counter = accCreateCounter.incrementAndGet();
		TestResponse res = request("POST", "/account", "{name:john" + counter + ",mobileNumber:"
				+ String.valueOf(MOBILE_NUMBER_START + counter) + ",balance:" + INIT_BALANCE + "}");
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);
		return json;
	}

	private String createAccountReturnAccountId() {
		Map<String, Object> json = createAccountRetJson();
		return (String) json.get(ACCOUNT_ID);
	}

	AtomicInteger accCreateCounter = new AtomicInteger(0);
	AtomicInteger transactionCreateCounter = new AtomicInteger(0);

	private Map<String, Object> verifyAccountCreation(Map<String, Object> json) {
		int currentCounterValue = accCreateCounter.get();
		assertEquals(NAME_PREFIX + currentCounterValue, json.get(NAME));

		assertEquals(String.valueOf(MOBILE_NUMBER_START + currentCounterValue), json.get(MOBILE_NUMBER));
		assertEquals("Status is not correct", "SUCCESS", json.get(STATUS));
		assertNotNull(json.get(ACCOUNT_ID));
		assertEquals("balance does not match", INIT_BALANCE, (double) json.get(BALANCE), 0.0001);
		return json;
	}
}
