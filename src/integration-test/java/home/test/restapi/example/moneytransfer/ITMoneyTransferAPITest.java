package home.test.restapi.example.moneytransfer;

import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.DEBIT_FAILED;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.INIT_BALANCE;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.STATUS;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.STATUS_ERROR;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.TRANSFER_TYPE_DEBIT;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.createAccountRetJson;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.createAccountReturnAccountId;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.getAccountStatus;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.transferAndRetJson;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.verifyAccountDetails;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.verifyTransaction;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.verifyTransactionFailed;
import static home.test.restapi.example.moneytransfer.MoneyTransferAPITestHelper.transferCashAndRetJson;
import static home.test.restapi.testtool.TestRequestHelper.request;
import static org.junit.Assert.assertEquals;

import java.util.Map;

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

	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MoneyTransferAPI.main(null);

		//just to make sure the server has been started completely
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
		assertEquals("Status is not correct", STATUS_ERROR, json.get(STATUS));
	}

	@Ignore
	@Test
	public void testAccountDelete() {
		
	}

	@Test
	public void testGetAccounts() {
		TestResponse response = request("GET", "/account", "");
		assertEquals(200, response.status);
		Map<String, Object> json = response.json();
		System.out.println("GETACCOUNTS");
		json.forEach((key,value) -> {
			System.out.println("---> KEY "+ key +" VALUE "+ value);
		});
		System.out.println("GETACCOUNTS END");
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
	
	@Test
	public void testTransactionSuccessfulDebitCash() {
		String accountId = createAccountReturnAccountId();
		
		Map<String, Object> json = transferCashAndRetJson(accountId, 30, TRANSFER_TYPE_DEBIT );
		verifyTransaction(accountId, json, null,  30);
	}

	@Test
	public void testTransactionSuccessfulCreditCash() {
		
	}
}
