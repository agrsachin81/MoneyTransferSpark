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

	private static final String DEBIT_FAILED = "DEBIT_FAILED";
	private static final String AMOUNT_TRANSACTED = "amount";
	private static final String CP_ACCOUNT_ID = "cpAccountId";
	private static final String TRANSACTION_REKUEST_ID = "transactionRekuestId";
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
		verifyAccountDetails(json);
	}

	@Test
	public void testAccountGet() {
		String accId = createAccountReturnAccountId();
		Map<String, Object> json = getAccountStatus(accId);
		verifyAccountDetails(json);
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
		
		Map<String, Object> json = transferAndRetJson(accountId, 30, accountId2);
		verifyTransaction(accountId, json, accountId2,  30);
	}

	@Test
	public void testTransactionFailedInsufficientFunds() {
		String accountId = createAccountReturnAccountId();
		String accountId2 = createAccountReturnAccountId();
		
		Map<String, Object> json = transferAndRetJson(accountId, INIT_BALANCE+0.1, accountId2);
		verifyTransactionFailed(accountId, json, accountId2,  INIT_BALANCE+0.1, DEBIT_FAILED);
	}
	
	@Ignore
	@Test
	public void testTransactionSuccessfulDebitCash() {
		
	}

	@Ignore
	@Test
	public void testTransactionSuccessfulCreditCash() {

	}
	
	private Map<String,Object> transferAndRetJson(String origAccountId, double amount, String cpAccountId) {
		int counter = transactionCreateCounter.incrementAndGet();
		
		TestResponse res = request("POST", "/account/"+origAccountId+"/transact", "{amount:" + amount + ",cpAccountId:"+cpAccountId
				+ ",transactionRekuestId:" +TRAN_REK_ID_PREFIX+ counter + "}");
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);
		return json;
	}
	
	private void verifyTransactionFailed(String origAccountId, Map<String, Object> json, String cpAccountId, double amount, String transactionStatus) {
		int currentCounterValue = transactionCreateCounter.get();
		assertEquals("Status is not correct", "ERROR", json.get(STATUS));
		assertEquals("Transaction status is not correct", transactionStatus, json.get("transactionStatus"));
		String accId = (String)json.get(ACCOUNT_ID);
		assertNotNull(accId);
		
		assertEquals("origAccId", origAccountId, accId);
		assertEquals("TransactionRek Id match ", TRAN_REK_ID_PREFIX+currentCounterValue , json.get(TRANSACTION_REKUEST_ID));
		assertEquals("cpAccId", cpAccountId, (String)json.get(CP_ACCOUNT_ID));
				
		double origAccountBalance= getAccountBalance(origAccountId);
		double cpAccountBalance = getAccountBalance(cpAccountId);
		assertEquals("origAccountBalance ", INIT_BALANCE , origAccountBalance,0.001);
		assertEquals("origAccountBalance ", INIT_BALANCE , cpAccountBalance,0.001);
	}
	
	private void verifyTransaction(String origAccountId, Map<String, Object> json, String cpAccountId, double amount) {
		int currentCounterValue = transactionCreateCounter.get();
		assertEquals("Status is not correct", "SUCCESS", json.get(STATUS));
		assertEquals("Transaction status is not correct", "DONE", json.get("transactionStatus"));
		String accId = (String)json.get(ACCOUNT_ID);
		assertNotNull(accId);
		
		assertEquals("origAccId does not match", origAccountId, accId);
		assertEquals("TransactionRek Id do not match ", TRAN_REK_ID_PREFIX+currentCounterValue , json.get(TRANSACTION_REKUEST_ID));
		assertEquals("cpAccId does not match ", cpAccountId, (String)json.get(CP_ACCOUNT_ID));
				
		double origAccountBalance= getAccountBalance(origAccountId);
		double cpAccountBalance = getAccountBalance(cpAccountId);
		assertEquals("origAccountBalance ", INIT_BALANCE -amount, origAccountBalance,0.001);
		assertEquals("origAccountBalance ", INIT_BALANCE +amount, cpAccountBalance,0.001);
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
	
	private Map<String, Object> getAccountStatus(String accId){
		TestResponse response = request("GET", "/account/" + accId, "");
		System.out.println(response.body);
		Map<String, Object> json = response.json();
		assertEquals(200, response.status);
		return json;
	}
	
	private double getAccountBalance(String accountId) {
		
		Map<String, Object> json = getAccountStatus(accountId);
		
		return (double)json.get(BALANCE);
	}

	AtomicInteger accCreateCounter = new AtomicInteger(0);
	AtomicInteger transactionCreateCounter = new AtomicInteger(0);

	private Map<String, Object> verifyAccountDetails(Map<String, Object> json) {
		int currentCounterValue = accCreateCounter.get();
		assertEquals(NAME_PREFIX + currentCounterValue, json.get(NAME));

		assertEquals(String.valueOf(MOBILE_NUMBER_START + currentCounterValue), json.get(MOBILE_NUMBER));
		assertEquals("Status is not correct", "SUCCESS", json.get(STATUS));
		assertNotNull(json.get(ACCOUNT_ID));
		assertEquals("balance does not match", INIT_BALANCE, (double) json.get(BALANCE), 0.0001);
		return json;
	}
}
